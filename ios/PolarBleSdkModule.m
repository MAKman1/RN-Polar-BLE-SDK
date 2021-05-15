#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(PolarBleSdkModule, RCTEventEmitter)

RCT_EXTERN_METHOD(supportedEvents)
RCT_EXTERN_METHOD(connectToDevice:(NSString *)deviceId)
RCT_EXTERN_METHOD(disconnectFromDevice:(NSString *)deviceId)
RCT_EXTERN_METHOD(startEcgStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(startAccStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(startPpgStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(startPpiStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(stopEcgStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(stopAccStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(stopPpgStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(stopPpiStreaming:(NSString *)deviceId)
RCT_EXTERN_METHOD(sampleMethod:(NSString *)stringArgument numberArgument:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)

@end
