
import Foundation
import CoreBluetooth
import RxSwift

public class CBDeviceListenerImpl: NSObject,
                            CBCentralManagerDelegate
{
    fileprivate lazy var manager = CBCentralManager(delegate: self, queue: queueBle, options: nil)
    fileprivate let sessions=AtomicList<CBDeviceSessionImpl>()
    fileprivate var queue: DispatchQueue
    fileprivate var queueBle: DispatchQueue
    fileprivate var connectionObservers = AtomicList<RxObserver<(session: BleDeviceSession, state: BleDeviceSession.DeviceSessionState)>>()
    fileprivate let powerObservers = AtomicList<RxObserver<BleState>>()
    fileprivate lazy var scanner = CBScanner(manager, queue: queue, sessions: sessions)
    fileprivate let factory: BleGattClientFactory
    public var automaticH10Mapping = false
    public var automaticReconnection = true
    public var scanPreFilter: ((_ content: BleAdvertisementContent) -> Bool)?
    public var servicesToScanFor: [CBUUID]? {
        didSet {
            scanner.setServices(servicesToScanFor)
        }
    }
    public var deviceSessionStateObserver: BleDeviceSessionStateObserver?
    public var powerStateObserver: BlePowerStateObserver? {
        didSet {
            powerStateObserver?.powerStateChanged(BleState(rawValue: self.manager.state.rawValue) ?? BleState.unknown)
        }
    }
    
    public init(_ queue: DispatchQueue, clients: [(_ transport: BleAttributeTransportProtocol) -> BleGattClientBase], identifier: Int){
        self.queue = queue
        self.factory = BleGattClientFactory(clients)
        self.queueBle = DispatchQueue(label: "CBDeviceListenerImplQueue\(identifier)", attributes: [])
        super.init()
    }
    
    fileprivate func session(_ peripheral: CBPeripheral) -> CBDeviceSessionImpl? {
        return sessions.fetch( { (item: CBDeviceSessionImpl) -> Bool in
                return (item.peripheral.identifier == peripheral.identifier)
            }
        )
    }
    
    fileprivate func updateSessionState(_ session: CBDeviceSessionImpl, state: BleDeviceSession.DeviceSessionState) {
        session.updateSessionState(state)
        RxUtils.emitNext(connectionObservers) { (object) in
            object.obs.onNext((session: session, state: state))
        }
        deviceSessionStateObserver?.stateChanged(session)
        if scanner.scanningNeeded() {
            scanner.enableScan()
        } else {
            scanner.disableScan()
        }
    }
    
    @available(iOS 10.0, *)
    fileprivate func btState2String(_ state: CBManagerState) -> String {
        switch state {
            case .unknown:
                return "Unknown"
            case .resetting:
                return "Resetting"
            case .unsupported:
                return "Unsupported"
            case .unauthorized:
                return "Unauthorized"
            case .poweredOff:
                return "PoweredOff"
            case .poweredOn:
                return "PoweredOn"
            @unknown default:
                return "Unknown"
        }
    }
    
    // cb central manager callbacks
    public func centralManagerDidUpdateState(_ central: CBCentralManager){
        queue.async(execute: {
            if #available(iOS 10.0, *) {
                BleLogger.trace("state update to: ", self.btState2String(central.state))
            }
            switch central.state {
                case .unknown:
                    break
                case .resetting:
                    fallthrough
                case .unsupported:
                    fallthrough
                case .unauthorized:
                    fallthrough
                case .poweredOff:
                    self.scanner.powerOff()
                    let sessionList = self.sessions.list()
                    for session in sessionList {
                        if session.state == .sessionOpening ||
                           session.state == .sessionOpen ||
                           session.state == .sessionClosing {
                            self.handleDisconnected(session)
                            session.reset()
                        }
                    }
                    if central.state == .resetting {
                        // clear device list
                        self.sessions.removeAll()
                    }
                    break
                case .poweredOn:
                    self.scanner.powerOn()
                @unknown default:
                    break
            }
            RxUtils.emitNext(self.powerObservers, emitter: { (observer) in
                observer.obs.onNext(BleState(rawValue: self.manager.state.rawValue) ?? BleState.unknown)
            })
            self.powerStateObserver?.powerStateChanged(BleState(rawValue: self.manager.state.rawValue) ?? BleState.unknown)
        })
    }
    
    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber){
        if self.session(peripheral) == nil, let filter = self.scanPreFilter {
            let c=BleAdvertisementContent()
            if peripheral.name != nil && advertisementData[CBAdvertisementDataLocalNameKey] == nil {
                var advData = [String : AnyObject]()
                advData[CBAdvertisementDataLocalNameKey] = peripheral.name as AnyObject?
                c.processAdvertisementData(Int32(RSSI.intValue), advertisementData: advData)
            }
            c.processAdvertisementData(Int32(RSSI.intValue), advertisementData: advertisementData as [String : AnyObject])
            if !filter(c) {
                return
            }
        }
        handleDeviceDiscovered(central, didDiscover: peripheral, advertisementData:
            advertisementData, rssi: RSSI)
    }
    
    private func handleDeviceDiscovered(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber){
        var session = self.session(peripheral)
        if automaticH10Mapping &&
            peripheral.name?.contains("H10") ?? false ||
            peripheral.name?.contains("H9") ?? false {
            let items=peripheral.name?.split(separator: " ").map(String.init)
            let deviceId = items?.last
            if let old = self.sessions.fetch({ (impl) -> Bool in
                return impl.advertisementContent.polarDeviceIdUntouched == deviceId
            }) {
                if old != session {
                    BleLogger.trace("found old h10 or H9, mapped to new")
                    old.assignNewPeripheral(peripheral)
                    sessions.remove { (impl) -> Bool in
                        return impl == session
                    }
                    session = old
                }
            }
        }
        if session == nil {
            self.sessions.append(CBDeviceSessionImpl(peripheral: peripheral, central: manager, scanner: self, factory: factory, queueBle: self.queueBle, queue: self.queue))
            BleLogger.trace("new peripheral discovered: ",peripheral.description)
        }
        guard let sess = self.session(peripheral) else {
            BleLogger.error("out of memory")
            return
        }
        queue.async(execute: {
            sess.advertisementContent.processAdvertisementData(RSSI.int32Value, advertisementData: advertisementData)
            RxUtils.emitNext(self.scanner.scanObservers) { (observer) in
                observer.obs.onNext(sess)
            }
            
            if sess.state == .sessionOpenPark {
                if sess.isConnectable() {
                    self.updateSessionState(sess, state: .sessionOpening)
                    self.manager.connect(sess.peripheral, options: nil)
                } else {
                    BleLogger.trace("connection attempt deferred due to reason device is non connectable")
                }
            }
        })
    }
    
    public func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral){
        BleLogger.trace("didConnect: ", peripheral.description)
        queue.async(execute: {
                if let device = self.session(peripheral){
                    device.connected()
                    self.updateSessionState(device,state: BleDeviceSession.DeviceSessionState.sessionOpen)
                } else {
                    BleLogger.error("didConnect: Unknown peripheral received")
                }
            }
        )
    }
    
    public func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?){
        BleLogger.trace("didFailToConnect: ", peripheral.description)
        queue.async(execute: {
                if let device = self.session(peripheral) {
                    self.handleDisconnected(device)
                } else {
                    BleLogger.error("didFailToConnect: Unknown peripheral received")
                }
            }
        )
    }
    
    public func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?){
        BleLogger.trace("didDisconnectPeripheral: ", peripheral.description)
        queue.async(execute: {
                if let device = self.session(peripheral) {
                    self.handleDisconnected(device)
                    device.reset()
                } else {
                    BleLogger.error("didDisconnectPeripheral: Unknown peripheral received")
                }
            }
        )
    }
    
    fileprivate func handleDisconnected(_ session: CBDeviceSessionImpl) {
        if automaticReconnection {
            switch (session.state) {
            case .sessionOpen where session.connectionType == .directConnection && self.blePowered():
                updateSessionState(session, state: .sessionOpening)
                manager.connect((session).peripheral, options: nil)
            case .sessionOpen: fallthrough
            case .sessionOpening:
                updateSessionState(session, state: .sessionOpenPark)
            case .sessionClosing:
                updateSessionState(session, state: .sessionClosed)
            default:
                break
            }
        } else {
            updateSessionState(session, state: .sessionClosed)
        }
    }
}

extension CBDeviceListenerImpl: CBScanningProtocol {
    public func stopScanning(){
        queue.async(execute: {
            self.scanner.stopScan()
        })
    }
    
    public func continueScanning(){
        queue.async(execute: {
            self.scanner.startScan()
        })
    }
}

extension CBDeviceListenerImpl: BleDeviceListener {
   
    public func blePowered() -> Bool{
        return manager.state == .poweredOn
    }
    
    public func monitorBleState() -> Observable<BleState>{
        var object: RxObserver<BleState>!
        return Observable.create{ observer in
            object = RxObserver<BleState>(obs: observer)
            object.obs.onNext(BleState(rawValue: self.manager.state.rawValue)!)
            self.powerObservers.append(object)
            return Disposables.create {
                BleLogger.trace("Power observer disposed")
                self.powerObservers.remove({ (item) -> Bool in
                    return item === object
                })
            }
        }
    }
    
    public func search(_ uuids: [CBUUID]?, identifiers: [UUID]?) -> Observable<BleDeviceSession> {
        var object: RxObserver<BleDeviceSession>!
        return Observable.create{ observer in
            object = RxObserver<BleDeviceSession>(obs: observer)
            self.scanner.addClient(object)
            var peripherals = [CBPeripheral]()
            if uuids != nil {
                peripherals = self.manager.retrieveConnectedPeripherals(withServices: uuids!)
            }
            if identifiers != nil {
                peripherals.append(contentsOf: self.manager.retrievePeripherals(withIdentifiers: (identifiers)!))
            }
            for device in peripherals {
                if self.session(device) == nil {
                    var advData = [String : Any]()
                    if device.name != nil {
                        advData[CBAdvertisementDataLocalNameKey] = device.name as Any?
                    }
                    if uuids != nil {
                        advData[CBAdvertisementDataServiceUUIDsKey] = uuids as Any?
                    }
                    self.centralManager(self.manager, didDiscover: device, advertisementData: advData, rssi: -20)
                }
            }
            return Disposables.create {
                self.scanner.removeClient(object)
            }
        }
    }
    
    public func openSessionDirect(_ session: BleDeviceSession){
        if self.blePowered() {
            switch session.state {
            case .sessionClosed where !session.isConnectable():
                updateSessionState(session as! CBDeviceSessionImpl, state: .sessionOpenPark)
            case .sessionClosed:
                updateSessionState(session as! CBDeviceSessionImpl, state: .sessionOpening)
                manager.connect((session as! CBDeviceSessionImpl).peripheral, options: nil)
            case .sessionClosing:
                updateSessionState(session as! CBDeviceSessionImpl, state: .sessionOpenPark)
            default:
                break
            }
        } else if session.state == .sessionClosed {
            updateSessionState(session as! CBDeviceSessionImpl, state: .sessionOpenPark)
        }
    }
    
    public func monitorDeviceSessionState() -> Observable<(session: BleDeviceSession, state: BleDeviceSession.DeviceSessionState)>{
        var object: RxObserver<(session: BleDeviceSession, state: BleDeviceSession.DeviceSessionState)>!
        return Observable.create{ observer in
            object = RxObserver.init(obs: observer)
            self.connectionObservers.append(object)
            return Disposables.create {
                self.connectionObservers.remove({ (obs) -> Bool in
                    return obs === object
                })
            }
        }
    }
    
    public func closeSessionDirect(_ session: BleDeviceSession){
        switch (session.state) {
        case .sessionOpening: fallthrough
        case .sessionOpen:
            updateSessionState(session as! CBDeviceSessionImpl, state: .sessionClosing)
            manager.cancelPeripheralConnection((session as! CBDeviceSessionImpl).peripheral)
        case .sessionOpenPark where session.previousState == .sessionClosing:
            updateSessionState(session as! CBDeviceSessionImpl, state: .sessionClosing)
        case .sessionOpenPark:
            updateSessionState(session as! CBDeviceSessionImpl, state: .sessionClosed)
        default: break
        }
    }
    
    public func removeAllSessions(_ inState: Set<BleDeviceSession.DeviceSessionState>) -> Int{
        return sessions.removeIf({ (_ session: CBDeviceSessionImpl) -> Bool in
            return inState.contains(session.state)
        })
    }
    
    public func removeAllSessions() -> Int{
        return removeAllSessions(Set(arrayLiteral: .sessionClosed,.sessionOpenPark))
    }
    
    public func allSessions() -> [BleDeviceSession]{
        return sessions.list()
    }
}
