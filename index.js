import { NativeModules, Platform } from 'react-native';

// todo :
// add an abstraction layer on top of this very rough api using a map of
// device instances (see how it's done in OpenHealthBandReactNativeApp) and emit
// events from them ? or KISS like this ?

const { PolarBleSdkModule } = NativeModules;

class PolarBleSdk {
  constructor() {
    // todo ?
  }

  ////////// CONNECTION

  connectToDevice(deviceId) {
    PolarBleSdkModule.connectToDevice(deviceId);
  }

  disconnectFromDevice(deviceId) {
    PolarBleSdkModule.disconnectFromDevice(deviceId);
  }

  ////////// ECG STREAMING

  startEcgStreaming(deviceId) {
    PolarBleSdkModule.startEcgStreaming(deviceId);
  }

  stopEcgStreaming(deviceId) {
    PolarBleSdkModule.stopEcgStreaming(deviceId);
  }

  ////////// ACCELEROMETER STREAMING

  startAccStreaming(deviceId) {
    PolarBleSdkModule.startAccStreaming(deviceId);    
  }

  stopAccStreaming(deviceId) {
    PolarBleSdkModule.stopAccStreaming(deviceId);    
  }

  ////////// PPG STREAMING

  startPpgStreaming(deviceId) {
    PolarBleSdkModule.startPpgStreaming(deviceId);    
  }

  stopPpgStreaming(deviceId) {
    PolarBleSdkModule.stopPpgStreaming(deviceId);    
  }

  ////////// PPI STREAMING

  startPpiStreaming(deviceId) {
    PolarBleSdkModule.startPpiStreaming(deviceId);    
  }

  stopPpiStreaming(deviceId) {
    PolarBleSdkModule.stopPpiStreaming(deviceId);    
  }

  ////////// OTHER

  sampleMethod(number, string, callback) {
    PolarBleSdkModule.sampleMethod(number, string, callback);
  }

  // addListener(event, callback) {
  //   // return polarEmitter.addListener(event, callback);
  // }
}

export default new PolarBleSdk();
// export default PolarBleSdkModule;
