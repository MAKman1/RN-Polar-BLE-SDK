// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pftp_request.proto

// This CPP symbol can be defined to use imports that match up to the framework
// imports needed when using CocoaPods.
#if !defined(GPB_USE_PROTOBUF_FRAMEWORK_IMPORTS)
 #define GPB_USE_PROTOBUF_FRAMEWORK_IMPORTS 0
#endif

#if GPB_USE_PROTOBUF_FRAMEWORK_IMPORTS
 #import <Protobuf/GPBProtocolBuffers.h>
#else
 #import "google/GPBProtocolBuffers.h"
#endif

#if GOOGLE_PROTOBUF_OBJC_VERSION < 30002
#error This file was generated by a newer version of protoc which is incompatible with your Protocol Buffer library sources.
#endif
#if 30002 < GOOGLE_PROTOBUF_OBJC_MIN_SUPPORTED_VERSION
#error This file was generated by an older version of protoc which is incompatible with your Protocol Buffer library sources.
#endif

// @@protoc_insertion_point(imports)

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"

CF_EXTERN_C_BEGIN

@class PbDate;
@class PbDuration;
@class PbTime;
GPB_ENUM_FWD_DECLARE(PbSampleType);

NS_ASSUME_NONNULL_BEGIN

#pragma mark - Enum PbPFtpQuery

/**
 *
 * Query types (SAGRFC53).
 * Query parameters, if any, are transmitted in the following data (SAGRFC56)
 * and defined below, individually per query type. Responses to queries are
 * defined in pftp_response.proto.
 **/
typedef GPB_ENUM(PbPFtpQuery) {
  PbPFtpQuery_IdentifyDevice = 0,
  PbPFtpQuery_SetSystemTime = 1,
  PbPFtpQuery_GetSystemTime = 2,
  PbPFtpQuery_SetLocalTime = 3,
  PbPFtpQuery_GetLocalTime = 4,
  PbPFtpQuery_GetDiskSpace = 5,
  PbPFtpQuery_GenerateChallengeToken = 6,
  PbPFtpQuery_SetInternalTest = 7,
  PbPFtpQuery_GetBatteryStatus = 8,
  PbPFtpQuery_SetAdbMode = 9,
  PbPFtpQuery_CleanupDiskSpace = 10,
  PbPFtpQuery_GetInactivityPreAlert = 11,
  PbPFtpQuery_PrepareFirmwareUpdate = 12,
  PbPFtpQuery_RequestSynchronization = 13,
  PbPFtpQuery_RequestStartRecording = 14,
  PbPFtpQuery_RequestStopRecording = 15,
  PbPFtpQuery_RequestRecordingStatus = 16,
};

GPBEnumDescriptor *PbPFtpQuery_EnumDescriptor(void);

/**
 * Checks to see if the given value is defined by the enum or was not known at
 * the time this source was generated.
 **/
BOOL PbPFtpQuery_IsValidValue(int32_t value);

#pragma mark - Enum PbPFtpOperation_Command

typedef GPB_ENUM(PbPFtpOperation_Command) {
  PbPFtpOperation_Command_Get = 0,
  PbPFtpOperation_Command_Put = 1,
  PbPFtpOperation_Command_Merge = 2,
  PbPFtpOperation_Command_Remove = 3,
};

GPBEnumDescriptor *PbPFtpOperation_Command_EnumDescriptor(void);

/**
 * Checks to see if the given value is defined by the enum or was not known at
 * the time this source was generated.
 **/
BOOL PbPFtpOperation_Command_IsValidValue(int32_t value);

#pragma mark - PftpRequestRoot

/**
 * Exposes the extension registry for this file.
 *
 * The base class provides:
 * @code
 *   + (GPBExtensionRegistry *)extensionRegistry;
 * @endcode
 * which is a @c GPBExtensionRegistry that includes all the extensions defined by
 * this file and all files that it depends on.
 **/
@interface PftpRequestRoot : GPBRootObject
@end

#pragma mark - PbPFtpOperation

typedef GPB_ENUM(PbPFtpOperation_FieldNumber) {
  PbPFtpOperation_FieldNumber_Command = 1,
  PbPFtpOperation_FieldNumber_Path = 2,
};

/**
 *
 * Class for PFTP filesystem operation and its parameters.
 * - For GET, ending directory delimiter in path designates directory listing operation. Otherwise, it's a read file operation.
 * - For PUT, ending directory delimiter in path designates directory creation. Otherwise, it's a write file operation.
 * - For PUT (when writing a file) and MERGE operations, the file is transmitted in following data.
 * - Other than PUT (when writing a file) and MERGE operations do not required additional data, only the path parameter.
 * - See SAGRFC56 for details.
 **/
@interface PbPFtpOperation : GPBMessage

/** File operation command. The format of the path parameter affect to the command behavior. See SAGRFC56. */
@property(nonatomic, readwrite) PbPFtpOperation_Command command;

@property(nonatomic, readwrite) BOOL hasCommand;
/** Path to the target file or directory. Ending directory delimiter designates directory. See SAGRFC56. */
@property(nonatomic, readwrite, copy, null_resettable) NSString *path;
/** Test to see if @c path has been set. */
@property(nonatomic, readwrite) BOOL hasPath;

@end

#pragma mark - PbPFtpSetSystemTimeParams

typedef GPB_ENUM(PbPFtpSetSystemTimeParams_FieldNumber) {
  PbPFtpSetSystemTimeParams_FieldNumber_Date = 1,
  PbPFtpSetSystemTimeParams_FieldNumber_Time = 2,
  PbPFtpSetSystemTimeParams_FieldNumber_Trusted = 3,
};

/**
 *
 * Parameters for SET_SYSTEM_TIME query.
 **/
@interface PbPFtpSetSystemTimeParams : GPBMessage

/** UTC date to be set. */
@property(nonatomic, readwrite, strong, null_resettable) PbDate *date;
/** Test to see if @c date has been set. */
@property(nonatomic, readwrite) BOOL hasDate;

/** UTC time to be set. */
@property(nonatomic, readwrite, strong, null_resettable) PbTime *time;
/** Test to see if @c time has been set. */
@property(nonatomic, readwrite) BOOL hasTime;

/** True, if date and time are from trusted source. */
@property(nonatomic, readwrite) BOOL trusted;

@property(nonatomic, readwrite) BOOL hasTrusted;
@end

#pragma mark - PbPFtpRequestStartRecordingParams

typedef GPB_ENUM(PbPFtpRequestStartRecordingParams_FieldNumber) {
  PbPFtpRequestStartRecordingParams_FieldNumber_SampleType = 1,
  PbPFtpRequestStartRecordingParams_FieldNumber_RecordingInterval = 2,
  PbPFtpRequestStartRecordingParams_FieldNumber_SampleDataIdentifier = 3,
};

/**
 *
 * Parameters for REQUEST_START_RECORDING query.
 **/
@interface PbPFtpRequestStartRecordingParams : GPBMessage

@property(nonatomic, readwrite) enum PbSampleType sampleType;

@property(nonatomic, readwrite) BOOL hasSampleType;
@property(nonatomic, readwrite, strong, null_resettable) PbDuration *recordingInterval;
/** Test to see if @c recordingInterval has been set. */
@property(nonatomic, readwrite) BOOL hasRecordingInterval;

@property(nonatomic, readwrite, copy, null_resettable) NSString *sampleDataIdentifier;
/** Test to see if @c sampleDataIdentifier has been set. */
@property(nonatomic, readwrite) BOOL hasSampleDataIdentifier;

@end

#pragma mark - PbPFtpSetLocalTimeParams

typedef GPB_ENUM(PbPFtpSetLocalTimeParams_FieldNumber) {
  PbPFtpSetLocalTimeParams_FieldNumber_Date = 1,
  PbPFtpSetLocalTimeParams_FieldNumber_Time = 2,
  PbPFtpSetLocalTimeParams_FieldNumber_TzOffset = 3,
};

/**
 *
 * Parameters for SET_LOCAL_TIME query.
 **/
@interface PbPFtpSetLocalTimeParams : GPBMessage

/** Local date to be set. */
@property(nonatomic, readwrite, strong, null_resettable) PbDate *date;
/** Test to see if @c date has been set. */
@property(nonatomic, readwrite) BOOL hasDate;

/** Local time to be set. */
@property(nonatomic, readwrite, strong, null_resettable) PbTime *time;
/** Test to see if @c time has been set. */
@property(nonatomic, readwrite) BOOL hasTime;

/** If present (known), timezone offset in 1 minute precision (-12h = -720min to +14h = +840min). */
@property(nonatomic, readwrite) int32_t tzOffset;

@property(nonatomic, readwrite) BOOL hasTzOffset;
@end

#pragma mark - PbPFtpGenerateChallengeTokenParams

typedef GPB_ENUM(PbPFtpGenerateChallengeTokenParams_FieldNumber) {
  PbPFtpGenerateChallengeTokenParams_FieldNumber_UserId = 1,
  PbPFtpGenerateChallengeTokenParams_FieldNumber_Nonse = 2,
};

/**
 *
 * Parameters for GENERATE_CHALLENGE_TOKEN query.
 **/
@interface PbPFtpGenerateChallengeTokenParams : GPBMessage

/** User's local id. */
@property(nonatomic, readwrite) uint32_t userId;

@property(nonatomic, readwrite) BOOL hasUserId;
/** Exactly 16 bytes of random nonse. */
@property(nonatomic, readwrite, copy, null_resettable) NSData *nonse;
/** Test to see if @c nonse has been set. */
@property(nonatomic, readwrite) BOOL hasNonse;

@end

#pragma mark - PbPFtpSetAdbModeParams

typedef GPB_ENUM(PbPFtpSetAdbModeParams_FieldNumber) {
  PbPFtpSetAdbModeParams_FieldNumber_Enable = 1,
};

/**
 *
 * Parameters for SET_ADB_MODE query.
 **/
@interface PbPFtpSetAdbModeParams : GPBMessage

/** Enable / disable adb mode */
@property(nonatomic, readwrite) BOOL enable;

@property(nonatomic, readwrite) BOOL hasEnable;
@end

#pragma mark - PbPFtpCleanupDiskSpaceParams

typedef GPB_ENUM(PbPFtpCleanupDiskSpaceParams_FieldNumber) {
  PbPFtpCleanupDiskSpaceParams_FieldNumber_RequiredBytes = 1,
};

/**
 *
 * Parameters for CLEANUP_DISK_SPACE query.
 **/
@interface PbPFtpCleanupDiskSpaceParams : GPBMessage

/** Amount of required free space in bytes */
@property(nonatomic, readwrite) uint64_t requiredBytes;

@property(nonatomic, readwrite) BOOL hasRequiredBytes;
@end

NS_ASSUME_NONNULL_END

CF_EXTERN_C_END

#pragma clang diagnostic pop

// @@protoc_insertion_point(global_scope)
