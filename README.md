# react-native-polar-ble-sdk

A React Native Wrapper for [Polar's BLE SDK](https://github.com/polarofficial/polar-ble-sdk)

## Getting started

This should work :

`$ npm install git+https://github.com/MAKman1/RN-Polar-BLE-SDK.git --save`

Though I only used this repo as a local git submodule at the moment


## Usage

```javascript
import {
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

import PolarBleSdk from 'react-native-polar-ble-sdk';

const polarEmitter = new NativeEventEmitter(NativeModules.PolarBleSdkModule);

////////// currently available events

polarEmitter.addListener('connectionState', ({ id, state }) => {
  console.log(`device ${id} connection state : ${state}`);
});

polarEmitter.addListener('firmwareVersion', ({ id, value }) => {
  console.log(`device ${id} firmware version : ${value}`);  
});

polarEmitter.addListener('batteryLevel', ({ id, value }) => {
  console.log(`device ${id} battery level : ${value}`);  
});

polarEmitter.addListener('ecgFeatureReady', ({ id }) => {
  console.log(`ecg feature ready on device ${id}`);    
});

polarEmitter.addListener('accelerometerFeatureReady', ({ id }) => {
  console.log(`accelerometer feature ready on device ${id}`);    
});

polarEmitter.addListener('ppgFeatureReady', ({ id }) => {
  console.log(`ppg feature ready on device ${id}`);      
});

polarEmitter.addListener('ppiFeatureReady', ({ id }) => {
  console.log(`ppi feature ready on device ${id}`);    
});

polarEmitter.addListener('hrData', (data) => {
  const {
    id,
    hr,
    contact,
    contactSupported,
    rrAvailable,
    rrs,
    rrsMs,  
  } = data;
});

polarEmitter.addListener('accData', (data) => {
  const { id, timeStamp, samples } = data;
  const { x, y, z } = samples[0];
});

polarEmitter.addListener('ecgData', (data) => {
  const { id, timeStamp, samples } = data;
  const value = samples[0];
});

polarEmitter.addListener('ppgData', (data) => {
  const { id, timeStamp, samples } = data;
  const { ppg0, ppg1, ppg2, ambient } = samples[0];
});

polarEmitter.addListener('ppiData', (data) => {
  const { id, timeStamp, samples } = data;
  const {
    hr,
    ppInMs,
    ppErrorEstimate,
    blockerBit,
    skinContactStatus,
    skinContactSupported,
  } = samples[0];
});

////////// currently available methods

// polar device's id also appearing in the advertised device name
const id = '12345XYZ';

// attempt to connect to device
PolarBleSdk.connectToDevice(id);

// now wait for the 'connectionState' event to be emitted with the right id
// and a 'connected' state value, then wait for corresponding 'xxxFeatureReady'
// event to be emitted with the right id before calling
// PolarBleSdk.startXxxStreaming() to start receiving the data from event
// 'xxxData' (except for hrData, which is emitted continuously by both H10 and
// OH1 as soon as the device is connected)

// will work with both H10 and OH1 devices
PolarBleSdk.startAccStreaming()
PolarBleSdk.stopAccStreaming();

// will only work with H10 devices
PolarBleSdk.startEcgStreaming();
PolarBleSdk.stopEcgStreaming();

// will only work with OH1 devices
PolarBleSdk.startPpgStreaming()
PolarBleSdk.stopPpgStreaming();
PolarBleSdk.startPpiStreaming();
PolarBleSdk.stopPpiStreaming();

// whenever you are done with the device you can disconnect it
// (active streams will be stopped automatically)
PolarBleSdk.disconnectFromDevice(id);
```

## iOS issues

Polar's BLE sdk relies on Carthage as a dependency manager, and you will need to
[install it](https://github.com/Carthage/Carthage#installing-carthage) in order
to rebuild the sdk if you use XCode >= 12.
The rebuild process is automated in `ios/scripts/build_frameworks.sh` which is
executed in the npm preinstall hook, but you still need to provide your admin
password manually at the end of the rebuild.

