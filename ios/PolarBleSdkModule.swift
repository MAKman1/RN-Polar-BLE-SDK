import Foundation
import PolarBleSdk
import RxSwift
import CoreBluetooth

struct Device {
  var hrReady: Bool = false
  var ecgReady: Bool = false
  var accReady: Bool = false
  var ppgReady: Bool = false
  var ppiReady: Bool = false
  var broadcast: Disposable?
  var ecgToggle: Disposable?
  var accToggle: Disposable?
  var ppgToggle: Disposable?
  var ppiToggle: Disposable?
  var searchToggle: Disposable?
  var autoConnect: Disposable?
  var entry: PolarExerciseEntry?
}

@objc(PolarBleSdkModule)
class PolarBleSdkModule : RCTEventEmitter,
                          PolarBleApiObserver,
                          PolarBleApiPowerStateObserver,
                          PolarBleApiDeviceInfoObserver,
                          PolarBleApiDeviceFeaturesObserver,
                          PolarBleApiDeviceHrObserver,
                          PolarBleApiCCCWriteObserver,
                          PolarBleApiLogger {

  public static var emitter : RCTEventEmitter!

  var api: PolarBleApi = PolarBleApiDefaultImpl.polarImplementation(
    DispatchQueue.main,
    features: Features.allFeatures.rawValue
  );
  // var api: PolarBleApi! = nil

  var devices: [String: Device] = [:]

  override init() {
    super.init()
    PolarBleSdkModule.emitter = self

    // api = PolarBleApiDefaultImpl.polarImplementation(
    //   DispatchQueue.main,
    //   features: Features.allFeatures.rawValue
    // );
    // guard api != nil else { return }

    api.observer = self
    api.powerStateObserver = self
    api.deviceInfoObserver = self
    api.deviceFeaturesObserver = self
    api.deviceHrObserver = self
    api.cccWriteObserver = self
    api.logger = self

    api.polarFilter(false)
    // NSLog("\(PolarBleApiDefaultImpl.versionInfo())")
  }

  override func supportedEvents() -> [String]! {
    return [
      "blePower",
      "connectionState",
      "hrFeatureReady",
      "ecgFeatureReady",
      "accelerometerFeatureReady",
      "ppgFeatureReady",
      "ppiFeatureReady",
      "firmwareVersion",
      "batteryLevel",
      "hrData",
      "ecgData",
      "accData",
      "ppgData",
      "ppiData"
    ]
  }

  override static func requiresMainQueueSetup() -> Bool {
    // module doesn't rely on UIKit so we return false
    // see https://reactnative.dev/docs/native-modules-ios
    return false
  }

  @objc func connectToDevice(_ id: String) -> Void {
    self.sendEvent(withName: "connectionState", body: [
      "id": id,
      "state": "scanning"
    ])

    do {
      try self.api.connectToDevice(id)
    } catch {
      self.sendEvent(withName: "connectionState", body: [
        "id": id,
        "state": "disconnected"
      ])
    }
  }

  @objc func disconnectFromDevice(_ id: String) -> Void {
    self.sendEvent(withName: "connectionState", body: [
      "id": id,
      "state": "disconnecting"
    ])

    do {
      try self.api.disconnectFromDevice(id)
    } catch {
      // e.g. if we are not already connected
      self.sendEvent(withName: "connectionState", body: [
        "id": id,
        "state": "disconnected"
      ])
    }
  }

  /**
    * we could use something like this in the following
    * start / stop streaming methods :
    *
    * guard var d = devices[id] else {
    *   // thow an exception or return;
    * }
    *
    * if (d.ecgReady && d.ecgToggle == nil) {
    *   // do the stuff
    * }
    */

  @objc func startEcgStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.ecgReady && d!.ecgToggle == nil {
      d!.ecgToggle = api.requestEcgSettings(id).asObservable().flatMap({
        (settings) -> Observable<PolarEcgData> in
        return self.api.startEcgStreaming(id, settings: settings.maxSettings())
      }).observeOn(MainScheduler.instance).subscribe { e in
        switch e {
        case .next(let data):
          let result: NSMutableDictionary = [:]
          result["id"] = id
          result["timeStamp"] = data.timeStamp
          let samples: NSMutableArray = []
          for µv in data.samples {
            samples.add(µv)
          }
          result["samples"] = samples
          self.sendEvent(withName: "ecgData", body: result)
        case .error(let err):
          NSLog("start ecg error: \(err)")
          d!.ecgToggle = nil
        case .completed:
          // NSLog("ecg finished")
          break
        }
      }
    }
  }

  @objc func stopEcgStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.ecgReady && d!.ecgToggle != nil {
      d!.ecgToggle?.dispose()
      d!.ecgToggle = nil
    }
  }

  @objc func startAccStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.accReady && d!.accToggle == nil {
      d!.accToggle = api.requestAccSettings(id).asObservable().flatMap({
        (settings) -> Observable<PolarAccData> in
        return self.api.startAccStreaming(id, settings: settings.maxSettings())
      }).observeOn(MainScheduler.instance).subscribe { e in
        switch e {
        case .next(let data):
          let result: NSMutableDictionary = [:]
          result["id"] = id
          result["timeStamp"] = data.timeStamp
          let samples: NSMutableArray = []
          for item in data.samples {
            let vec: NSMutableDictionary = [:]
            vec["x"] = item.x
            vec["y"] = item.y
            vec["z"] = item.z
            samples.add(vec)
          }
          result["samples"] = samples
          self.sendEvent(withName: "accData", body: result)
        case .error(let err):
          NSLog("start accelerometer error: \(err)")
          d!.accToggle = nil
        case .completed:
          // NSLog("acc finished")
          break
        }
      }
    }
  }

  @objc func stopAccStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.accReady && d!.accToggle != nil {
      d!.accToggle?.dispose()
      d!.accToggle = nil            
    }
  }

  @objc func startPpgStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.ppgReady && d!.ppgToggle == nil {
      d!.ppgToggle = api.requestPpgSettings(id).asObservable().flatMap({
        (settings) -> Observable<PolarPpgData> in
        return self.api.startOhrPPGStreaming(id, settings: settings.maxSettings())
      }).observeOn(MainScheduler.instance).subscribe { e in
        switch e {
        case .next(let data):
          let result: NSMutableDictionary = [:]
          result["id"] = id
          let samples: NSMutableArray = []
          for item in data.samples {
            let ppg: NSMutableDictionary = [:]
            ppg["ppg0"] = item.ppg0
            ppg["ppg1"] = item.ppg1
            ppg["ppg2"] = item.ppg2
            ppg["ambient"] = item.ambient
            samples.add(ppg)
          }
          result["samples"] = samples
          self.sendEvent(withName: "ppgData", body: result)
        case .error(let err):
          NSLog("start ppg error: \(err)")
          d!.ppgToggle = nil
        case .completed:
          // NSLog("ppg finished")
          break
        }
      }
    }        
  }

  @objc func stopPpgStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.ppgReady && d!.ppgToggle != nil {
      d!.ppgToggle?.dispose()
      d!.ppgToggle = nil        
    }
  }

  @objc func startPpiStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.ppiReady && d!.ppiToggle == nil {
      d!.ppiToggle = api.startOhrPPIStreaming(id).observeOn(MainScheduler.instance).subscribe { e in
        switch e {
        case .next(let data):
          let result: NSMutableDictionary = [:]
          result["id"] = id
          result["timeStamp"] = data.timeStamp
          let samples: NSMutableArray = []
          for item in data.samples {
            // NSLog("PPI: \(item.ppInMs)")
            /*
            let sample: NSMutableDictionary = [:]
            sample["hr"] = item.hr
            sample["ppInMs"] = item.ppInMs
            sample["ppErrorEstimate"] = item.ppErrorEstimate
            sample["blockerBit"] = item.blockerBit
            sample["skinContactStatus"] = item.skinContactStatus
            sample["skinContactSupported"] = item.skinContactSupported
            samples.add(sample)
            */
            samples.add(item)
          }
          result["samples"] = samples
          self.sendEvent(withName: "ppiData", body: result)
        case .error(let err):
          NSLog("start ppi error: \(err)")
          d!.ppiToggle = nil
        case .completed:
          // NSLog("ppi complete")
          break
        }
      }
    }
  }

  @objc func stopPpiStreaming(_ id: String) -> Void {
    var d = devices[id]
    if d != nil && d!.ppiReady && d!.ppiToggle != nil {
      d!.ppiToggle?.dispose()
      d!.ppiToggle = nil
    }
  }

  // CALLBACK EXAMPLE :

  @objc(sampleMethod:numberArgument:callback:)
  func sampleMethod(_ stringArgument: String,
                    numberArgument: NSNumber,
                    callback: RCTResponseSenderBlock) -> Void {
    // %@ formats any NSObject to its string representation
    let res = String(
      format: "number argument: %@, string argument: %@",
      numberArgument,
      stringArgument
    )

    callback([NSNull(), res])
  }

  //////////////////// OVERRIDDEN CALLBACK METHODS FROM VARIOUS PARENT CLASSES

  // PolarBleApiObserver
  func deviceConnecting(_ polarDeviceInfo: PolarDeviceInfo) {
    // NSLog("DEVICE CONNECTING: \(polarDeviceInfo)")
    self.sendEvent(withName: "connectionState", body: [
      "id": polarDeviceInfo.deviceId,
      "state": "connecting"
    ])
  }
  
  func deviceConnected(_ polarDeviceInfo: PolarDeviceInfo) {
    // NSLog("DEVICE CONNECTED: \(polarDeviceInfo)")
    let id = polarDeviceInfo.deviceId
    // self.devices.insert(id)
    self.devices[id] = Device()
    self.sendEvent(withName: "connectionState", body: [
      "id": id,
      "state": "connected"
    ])
  }
  
  func deviceDisconnected(_ polarDeviceInfo: PolarDeviceInfo) {
    // NSLog("DISCONNECTED: \(polarDeviceInfo)")
    let id = polarDeviceInfo.deviceId
    // self.devices.remove(id)
    self.devices[id] = nil
    self.sendEvent(withName: "connectionState", body: [
      "id": id,
      "state": "disconnected"
    ])
  }
  
  // PolarBleApiDeviceInfoObserver
  func batteryLevelReceived(_ id: String, batteryLevel: UInt) {
    // NSLog("battery level updated: \(batteryLevel)")
    self.sendEvent(withName: "batteryLevel", body: [
      "id": id,
      "value": batteryLevel
    ])
  }
  
  func disInformationReceived(_ id: String, uuid: CBUUID, value: String) {
    // NSLog("dis info: \(uuid.uuidString) value: \(value)")
    if uuid.uuidString == "00002a28-0000-1000-8000-00805f9b34fb" {
      let v = value.trimmingCharacters(in: .whitespacesAndNewlines)
      self.sendEvent(withName: "firmwareVersion", body: ["id": id, "value": v])
    }
  }
  
  // PolarBleApiDeviceHrObserver
  func hrValueReceived(_ id: String, data: PolarHrData) {
    let result: NSMutableDictionary = [:]

    result["id"] = id
    result["hr"] = data.hr
    result["contact"] = data.contact
    result["contactSupported"] = data.contactSupported
    // no rrAvailable property for ios (android only ?)

    let rrs: NSMutableArray = []
    for item in data.rrs {
      rrs.add(item)
    }
    result["rrs"] = rrs

    let rrsMs: NSMutableArray = []
    for item in data.rrsMs {
      rrsMs.add(item)
    }
    result["rrsMs"] = rrsMs

    self.sendEvent(withName: "hrData", body: result)
  }
  
  func hrFeatureReady(_ id: String) {
    // NSLog("HR READY")
    devices[id]?.hrReady = true
    self.sendEvent(withName: "hrFeatureReady", body: ["id": id])
  }
  
  // PolarBleApiDeviceEcgObserver
  func ecgFeatureReady(_ id: String) {
    // NSLog("ECG READY \(identifier)")
    devices[id]?.ecgReady = true
    self.sendEvent(withName: "ecgFeatureReady", body: ["id": id])
  }
  
  // PolarBleApiDeviceAccelerometerObserver
  func accFeatureReady(_ id: String) {
    // NSLog("ACC READY")
    devices[id]?.accReady = true
    self.sendEvent(withName: "accelerometerFeatureReady", body: ["id": id])
  }
  
  func ohrPPGFeatureReady(_ id: String) {
    // NSLog("OHR PPG ready")
    devices[id]?.ppgReady = true
    self.sendEvent(withName: "ppgFeatureReady", body: ["id": id])
  }
  
  // PPI
  func ohrPPIFeatureReady(_ id: String) {
    // NSLog("PPI Feature ready")
    devices[id]?.ppiReady = true
    self.sendEvent(withName: "ppiFeatureReady", body: ["id": id])
  }

  // PolarBleApiPowerStateObserver
  func blePowerOn() {
    // NSLog("BLE ON")
    // self.sendEvent(withName: "blePower", body: [
    //   "id": deviceId,
    //   "state": true
    // ])
  }
  
  func blePowerOff() {
    // NSLog("BLE OFF")
    // self.sendEvent(withName: "blePower", body: [
    //   "id": deviceId,
    //   "state": false
    // ])
  }    

  func ftpFeatureReady(_ identifier: String) {
    // NSLog("FTP ready")
  }
  
  func message(_ str: String) {
    // NSLog(str)
  }
  
  /// ccc write observer
  func cccWrite(_ address: UUID, characteristic: CBUUID) {
    // NSLog("ccc write: \(address) chr: \(characteristic)")
  }
}
