// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: exercise_rr_samples.proto

// This CPP symbol can be defined to use imports that match up to the framework
// imports needed when using CocoaPods.
#if !defined(GPB_USE_PROTOBUF_FRAMEWORK_IMPORTS)
 #define GPB_USE_PROTOBUF_FRAMEWORK_IMPORTS 0
#endif

#if GPB_USE_PROTOBUF_FRAMEWORK_IMPORTS
 #import <Protobuf/GPBProtocolBuffers_RuntimeSupport.h>
#else
 #import "GPBProtocolBuffers_RuntimeSupport.h"
#endif

 #import "ExerciseRrSamples.pbobjc.h"
 #import "Types.pbobjc.h"
 #import "Structures.pbobjc.h"
 #import "Nanopb.pbobjc.h"
// @@protoc_insertion_point(imports)

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"

#pragma mark - ExerciseRrSamplesRoot

@implementation ExerciseRrSamplesRoot

+ (GPBExtensionRegistry*)extensionRegistry {
  // This is called by +initialize so there is no need to worry
  // about thread safety and initialization of registry.
  static GPBExtensionRegistry* registry = nil;
  if (!registry) {
    GPB_DEBUG_CHECK_RUNTIME_VERSIONS();
    registry = [[GPBExtensionRegistry alloc] init];
    // Merge in the imports (direct or indirect) that defined extensions.
    [registry addExtensions:[TypesRoot extensionRegistry]];
    [registry addExtensions:[NanopbRoot extensionRegistry]];
  }
  return registry;
}

@end

#pragma mark - ExerciseRrSamplesRoot_FileDescriptor

static GPBFileDescriptor *ExerciseRrSamplesRoot_FileDescriptor(void) {
  // This is called by +initialize so there is no need to worry
  // about thread safety of the singleton.
  static GPBFileDescriptor *descriptor = NULL;
  if (!descriptor) {
    GPB_DEBUG_CHECK_RUNTIME_VERSIONS();
    descriptor = [[GPBFileDescriptor alloc] initWithPackage:@"data"
                                                     syntax:GPBFileSyntaxProto2];
  }
  return descriptor;
}

#pragma mark - PbRROffline

@implementation PbRROffline

@dynamic hasStartTime, startTime;
@dynamic hasTimeInterval, timeInterval;

typedef struct PbRROffline__storage_ {
  uint32_t _has_storage_[1];
  PbDuration *startTime;
  PbDuration *timeInterval;
} PbRROffline__storage_;

// This method is threadsafe because it is initially called
// in +initialize for each subclass.
+ (GPBDescriptor *)descriptor {
  static GPBDescriptor *descriptor = nil;
  if (!descriptor) {
    static GPBMessageFieldDescription fields[] = {
      {
        .name = "startTime",
        .dataTypeSpecific.className = GPBStringifySymbol(PbDuration),
        .number = PbRROffline_FieldNumber_StartTime,
        .hasIndex = 0,
        .offset = (uint32_t)offsetof(PbRROffline__storage_, startTime),
        .flags = GPBFieldRequired,
        .dataType = GPBDataTypeMessage,
      },
      {
        .name = "timeInterval",
        .dataTypeSpecific.className = GPBStringifySymbol(PbDuration),
        .number = PbRROffline_FieldNumber_TimeInterval,
        .hasIndex = 1,
        .offset = (uint32_t)offsetof(PbRROffline__storage_, timeInterval),
        .flags = GPBFieldRequired,
        .dataType = GPBDataTypeMessage,
      },
    };
    GPBDescriptor *localDescriptor =
        [GPBDescriptor allocDescriptorForClass:[PbRROffline class]
                                     rootClass:[ExerciseRrSamplesRoot class]
                                          file:ExerciseRrSamplesRoot_FileDescriptor()
                                        fields:fields
                                    fieldCount:(uint32_t)(sizeof(fields) / sizeof(GPBMessageFieldDescription))
                                   storageSize:sizeof(PbRROffline__storage_)
                                         flags:GPBDescriptorInitializationFlag_None];
    NSAssert(descriptor == nil, @"Startup recursed!");
    descriptor = localDescriptor;
  }
  return descriptor;
}

@end

#pragma mark - PbExerciseRRIntervals

@implementation PbExerciseRRIntervals

@dynamic rrIntervalsArray, rrIntervalsArray_Count;
@dynamic rrSensorOfflineArray, rrSensorOfflineArray_Count;

typedef struct PbExerciseRRIntervals__storage_ {
  uint32_t _has_storage_[1];
  GPBUInt32Array *rrIntervalsArray;
  NSMutableArray *rrSensorOfflineArray;
} PbExerciseRRIntervals__storage_;

// This method is threadsafe because it is initially called
// in +initialize for each subclass.
+ (GPBDescriptor *)descriptor {
  static GPBDescriptor *descriptor = nil;
  if (!descriptor) {
    static GPBMessageFieldDescription fields[] = {
      {
        .name = "rrIntervalsArray",
        .dataTypeSpecific.className = NULL,
        .number = PbExerciseRRIntervals_FieldNumber_RrIntervalsArray,
        .hasIndex = GPBNoHasBit,
        .offset = (uint32_t)offsetof(PbExerciseRRIntervals__storage_, rrIntervalsArray),
        .flags = (GPBFieldFlags)(GPBFieldRepeated | GPBFieldPacked),
        .dataType = GPBDataTypeUInt32,
      },
      {
        .name = "rrSensorOfflineArray",
        .dataTypeSpecific.className = GPBStringifySymbol(PbRROffline),
        .number = PbExerciseRRIntervals_FieldNumber_RrSensorOfflineArray,
        .hasIndex = GPBNoHasBit,
        .offset = (uint32_t)offsetof(PbExerciseRRIntervals__storage_, rrSensorOfflineArray),
        .flags = GPBFieldRepeated,
        .dataType = GPBDataTypeMessage,
      },
    };
    GPBDescriptor *localDescriptor =
        [GPBDescriptor allocDescriptorForClass:[PbExerciseRRIntervals class]
                                     rootClass:[ExerciseRrSamplesRoot class]
                                          file:ExerciseRrSamplesRoot_FileDescriptor()
                                        fields:fields
                                    fieldCount:(uint32_t)(sizeof(fields) / sizeof(GPBMessageFieldDescription))
                                   storageSize:sizeof(PbExerciseRRIntervals__storage_)
                                         flags:GPBDescriptorInitializationFlag_None];
    NSAssert(descriptor == nil, @"Startup recursed!");
    descriptor = localDescriptor;
  }
  return descriptor;
}

@end


#pragma clang diagnostic pop

// @@protoc_insertion_point(global_scope)
