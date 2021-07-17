// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: exercise_samples.proto

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

@class PbCalibrationValue;
@class PbDuration;
@class PbExerciseIntervalledSampleList;
@class PbExerciseRRIntervals;
@class PbPauseTime;
@class PbPowerMeasurements;
@class PbSampleSource;
@class PbSensorOffline;
GPB_ENUM_FWD_DECLARE(PbMovingType);
GPB_ENUM_FWD_DECLARE(PbOperationType);
GPB_ENUM_FWD_DECLARE(PbSampleType);

NS_ASSUME_NONNULL_BEGIN

#pragma mark - ExerciseSamplesRoot

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
@interface ExerciseSamplesRoot : GPBRootObject
@end

#pragma mark - PbPowerMeasurements

typedef GPB_ENUM(PbPowerMeasurements_FieldNumber) {
  PbPowerMeasurements_FieldNumber_CurrentPower = 1,
  PbPowerMeasurements_FieldNumber_CumulativeCrankRevolutions = 2,
  PbPowerMeasurements_FieldNumber_CumulativeTimestamp = 3,
  PbPowerMeasurements_FieldNumber_ForceMagnitudeMin = 4,
  PbPowerMeasurements_FieldNumber_ForceMagnitudeMax = 5,
  PbPowerMeasurements_FieldNumber_ForceMagnitudeMinAngle = 6,
  PbPowerMeasurements_FieldNumber_ForceMagnitudeMaxAngle = 7,
  PbPowerMeasurements_FieldNumber_BottomDeadSpotAngle = 8,
  PbPowerMeasurements_FieldNumber_TopDeadSpotAngle = 9,
  PbPowerMeasurements_FieldNumber_PedalPowerBalance = 10,
  PbPowerMeasurements_FieldNumber_TorqueMagnitudeMin = 11,
  PbPowerMeasurements_FieldNumber_TorqueMagnitudeMax = 12,
};

/**
 *
 * Power data from crank based power sensors
 * See: https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.cycling_power_measurement.xml
 **/
@interface PbPowerMeasurements : GPBMessage

/** Instantaneous power */
@property(nonatomic, readwrite) int32_t currentPower;

@property(nonatomic, readwrite) BOOL hasCurrentPower;
/**
 * Cumulative crank revolutions starting from the first stored sample data
 * This is originally overflowing 16 bit unsigned int value
 * This value do not necessarily change for every vector data set
 * This value is not necessarily provided for every vector data set
 **/
@property(nonatomic, readwrite) uint32_t cumulativeCrankRevolutions;

@property(nonatomic, readwrite) BOOL hasCumulativeCrankRevolutions;
/**
 * Cumulative timestamp in milliseconds starting from the first stored sample data
 * Originally overflowing uint16 value, [1/1024 of second]
 * This value do not necessarily change for every vector data set
 * This value is not necessarily provided for every vector data set
 **/
@property(nonatomic, readwrite) uint32_t cumulativeTimestamp;

@property(nonatomic, readwrite) BOOL hasCumulativeTimestamp;
/** Minimum force magnitude power */
@property(nonatomic, readwrite) int32_t forceMagnitudeMin;

@property(nonatomic, readwrite) BOOL hasForceMagnitudeMin;
/** Maximum force magnitude power */
@property(nonatomic, readwrite) int32_t forceMagnitudeMax;

@property(nonatomic, readwrite) BOOL hasForceMagnitudeMax;
/**
 * Minimum force magnitude angle
 * Counted clockwise, topmost position is 0 degrees, view point is right side of the bike for both pedals
 **/
@property(nonatomic, readwrite) uint32_t forceMagnitudeMinAngle;

@property(nonatomic, readwrite) BOOL hasForceMagnitudeMinAngle;
/**
 * Maximum force magnitude angle
 * Counted clockwise, topmost position is 0 degrees, view point is right side of the bike for both pedals
 **/
@property(nonatomic, readwrite) uint32_t forceMagnitudeMaxAngle;

@property(nonatomic, readwrite) BOOL hasForceMagnitudeMaxAngle;
/**
 * Dead spot bottom angle
 * Counted clockwise, topmost position is 0 degrees, view point is right side of the bike for both pedals
 **/
@property(nonatomic, readwrite) uint32_t bottomDeadSpotAngle;

@property(nonatomic, readwrite) BOOL hasBottomDeadSpotAngle;
/**
 * Dead spot top angle
 * Counted clockwise, topmost position is 0 degrees, view point is right side of the bike for both pedals
 **/
@property(nonatomic, readwrite) uint32_t topDeadSpotAngle;

@property(nonatomic, readwrite) BOOL hasTopDeadSpotAngle;
/**
 * Unit is in percentage with a resolution of 1/2. Left foot power percentage of total power.
 * pedal_power_balance = [LeftPower/(LeftPower + RightPower)]*100
 **/
@property(nonatomic, readwrite) uint32_t pedalPowerBalance;

@property(nonatomic, readwrite) BOOL hasPedalPowerBalance;
/** Unit is in newton metres with a resolution of 1/32. */
@property(nonatomic, readwrite) int32_t torqueMagnitudeMin;

@property(nonatomic, readwrite) BOOL hasTorqueMagnitudeMin;
/** Unit is in newton metres with a resolution of 1/32. */
@property(nonatomic, readwrite) int32_t torqueMagnitudeMax;

@property(nonatomic, readwrite) BOOL hasTorqueMagnitudeMax;
@end

#pragma mark - PbCalibrationValue

typedef GPB_ENUM(PbCalibrationValue_FieldNumber) {
  PbCalibrationValue_FieldNumber_StartIndex = 1,
  PbCalibrationValue_FieldNumber_Value = 2,
  PbCalibrationValue_FieldNumber_Operation = 3,
  PbCalibrationValue_FieldNumber_Cause = 4,
};

/**
 *
 * Sample data calibration value of the exercise
 **/
@interface PbCalibrationValue : GPBMessage

/** index of first sample using calibration value "value" */
@property(nonatomic, readwrite) uint32_t startIndex;

@property(nonatomic, readwrite) BOOL hasStartIndex;
/** used calibaration value */
@property(nonatomic, readwrite) float value;

@property(nonatomic, readwrite) BOOL hasValue;
/** used operation type of calibration (multiply/sum) */
@property(nonatomic, readwrite) enum PbOperationType operation;

@property(nonatomic, readwrite) BOOL hasOperation;
/** explaines the cause for calibration change (walking/running) */
@property(nonatomic, readwrite) enum PbMovingType cause;

@property(nonatomic, readwrite) BOOL hasCause;
@end

#pragma mark - PbExerciseIntervalledSampleList

typedef GPB_ENUM(PbExerciseIntervalledSampleList_FieldNumber) {
  PbExerciseIntervalledSampleList_FieldNumber_SampleType = 1,
  PbExerciseIntervalledSampleList_FieldNumber_RecordingIntervalMs = 2,
  PbExerciseIntervalledSampleList_FieldNumber_SampleSourceArray = 3,
  PbExerciseIntervalledSampleList_FieldNumber_HeartRateSamplesArray = 4,
  PbExerciseIntervalledSampleList_FieldNumber_CadenceSamplesArray = 5,
  PbExerciseIntervalledSampleList_FieldNumber_SpeedSamplesArray = 6,
  PbExerciseIntervalledSampleList_FieldNumber_DistanceSamplesArray = 7,
  PbExerciseIntervalledSampleList_FieldNumber_ForwardAccelerationArray = 8,
  PbExerciseIntervalledSampleList_FieldNumber_MovingTypeSamplesArray = 9,
  PbExerciseIntervalledSampleList_FieldNumber_AltitudeSamplesArray = 10,
  PbExerciseIntervalledSampleList_FieldNumber_AltitudeCalibrationArray = 11,
  PbExerciseIntervalledSampleList_FieldNumber_TemperatureSamplesArray = 12,
  PbExerciseIntervalledSampleList_FieldNumber_StrideLengthSamplesArray = 13,
  PbExerciseIntervalledSampleList_FieldNumber_StrideCalibrationArray = 14,
  PbExerciseIntervalledSampleList_FieldNumber_LeftPedalPowerSamplesArray = 15,
  PbExerciseIntervalledSampleList_FieldNumber_RightPedalPowerSamplesArray = 16,
  PbExerciseIntervalledSampleList_FieldNumber_LeftPowerCalibrationArray = 17,
  PbExerciseIntervalledSampleList_FieldNumber_RightPowerCalibrationArray = 18,
  PbExerciseIntervalledSampleList_FieldNumber_RrSamples = 19,
  PbExerciseIntervalledSampleList_FieldNumber_AccelerationMadSamplesArray = 20,
};

/**
 *
 * Exercise samples with sample type specific recording intervals
 **/
@interface PbExerciseIntervalledSampleList : GPBMessage

/** Sample Type, SPEED, DISTANCE etc. */
@property(nonatomic, readwrite) enum PbSampleType sampleType;

@property(nonatomic, readwrite) BOOL hasSampleType;
/**
 * Recording interval of samples in milliseconds
 * Note: When this field is set, it will override the default recording interval
 *       (PbDuration recording_interval) given in the PbExerciseSamples message
 **/
@property(nonatomic, readwrite) uint32_t recordingIntervalMs;

@property(nonatomic, readwrite) BOOL hasRecordingIntervalMs;
/** Indicates the source of certain sample: source and start / stop indexes for the given source */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSampleSource*> *sampleSourceArray;
/** The number of items in @c sampleSourceArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger sampleSourceArray_Count;

/** heart rate samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBUInt32Array *heartRateSamplesArray;
/** The number of items in @c heartRateSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger heartRateSamplesArray_Count;

/** cadence samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBUInt32Array *cadenceSamplesArray;
/** The number of items in @c cadenceSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger cadenceSamplesArray_Count;

/**
 * speed samples
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *speedSamplesArray;
/** The number of items in @c speedSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger speedSamplesArray_Count;

/**
 * distance samples: total distance from the beginning of the exercise
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *distanceSamplesArray;
/** The number of items in @c distanceSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger distanceSamplesArray_Count;

/** User 1d acceleration samples as m/s2 */
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *forwardAccelerationArray;
/** The number of items in @c forwardAccelerationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger forwardAccelerationArray_Count;

/** User walking/running/standing status samples */
// |movingTypeSamplesArray| contains |PbMovingType|
@property(nonatomic, readwrite, strong, null_resettable) GPBEnumArray *movingTypeSamplesArray;
/** The number of items in @c movingTypeSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger movingTypeSamplesArray_Count;

/**
 * altitude samples
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *altitudeSamplesArray;
/** The number of items in @c altitudeSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger altitudeSamplesArray_Count;

/** indicate start and stop indexes, used calibration value and operation type of calibration */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *altitudeCalibrationArray;
/** The number of items in @c altitudeCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger altitudeCalibrationArray_Count;

/** temperature samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *temperatureSamplesArray;
/** The number of items in @c temperatureSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger temperatureSamplesArray_Count;

/**
 * stride length samples
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBUInt32Array *strideLengthSamplesArray;
/** The number of items in @c strideLengthSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger strideLengthSamplesArray_Count;

/** indicate the information of the stride sensor calibration */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *strideCalibrationArray;
/** The number of items in @c strideCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger strideCalibrationArray_Count;

/** Crank based power samples from left pedal */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbPowerMeasurements*> *leftPedalPowerSamplesArray;
/** The number of items in @c leftPedalPowerSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger leftPedalPowerSamplesArray_Count;

/** Crank based power samples from right pedal */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbPowerMeasurements*> *rightPedalPowerSamplesArray;
/** The number of items in @c rightPedalPowerSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger rightPedalPowerSamplesArray_Count;

/**
 * Indicate the information of the bike power sensor calibration
 * This field is most likely used only for testing purposes
 **/
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *leftPowerCalibrationArray;
/** The number of items in @c leftPowerCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger leftPowerCalibrationArray_Count;

/**
 * Indicate the information of the bike power sensor calibration
 * This field is most likely used only for testing purposes
 **/
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *rightPowerCalibrationArray;
/** The number of items in @c rightPowerCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger rightPowerCalibrationArray_Count;

/** Heart Rate R-R sample data. */
@property(nonatomic, readwrite, strong, null_resettable) PbExerciseRRIntervals *rrSamples;
/** Test to see if @c rrSamples has been set. */
@property(nonatomic, readwrite) BOOL hasRrSamples;

/** Acceleration based Mean Amplitude Deviation (MAD) samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *accelerationMadSamplesArray;
/** The number of items in @c accelerationMadSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger accelerationMadSamplesArray_Count;

@end

#pragma mark - PbExerciseSamples

typedef GPB_ENUM(PbExerciseSamples_FieldNumber) {
  PbExerciseSamples_FieldNumber_RecordingInterval = 1,
  PbExerciseSamples_FieldNumber_HeartRateSamplesArray = 2,
  PbExerciseSamples_FieldNumber_HeartRateOfflineArray = 3,
  PbExerciseSamples_FieldNumber_CadenceSamplesArray = 4,
  PbExerciseSamples_FieldNumber_CadenceOfflineArray = 5,
  PbExerciseSamples_FieldNumber_AltitudeSamplesArray = 6,
  PbExerciseSamples_FieldNumber_AltitudeCalibrationArray = 7,
  PbExerciseSamples_FieldNumber_TemperatureSamplesArray = 8,
  PbExerciseSamples_FieldNumber_SpeedSamplesArray = 9,
  PbExerciseSamples_FieldNumber_SpeedOfflineArray = 10,
  PbExerciseSamples_FieldNumber_DistanceSamplesArray = 11,
  PbExerciseSamples_FieldNumber_DistanceOfflineArray = 12,
  PbExerciseSamples_FieldNumber_StrideLengthSamplesArray = 13,
  PbExerciseSamples_FieldNumber_StrideLengthOfflineArray = 14,
  PbExerciseSamples_FieldNumber_StrideCalibrationArray = 15,
  PbExerciseSamples_FieldNumber_ForwardAccelerationArray = 16,
  PbExerciseSamples_FieldNumber_MovingTypeSamplesArray = 17,
  PbExerciseSamples_FieldNumber_AltitudeOfflineArray = 18,
  PbExerciseSamples_FieldNumber_TemperatureOfflineArray = 19,
  PbExerciseSamples_FieldNumber_ForwardAccelerationOfflineArray = 20,
  PbExerciseSamples_FieldNumber_MovingTypeOfflineArray = 21,
  PbExerciseSamples_FieldNumber_LeftPedalPowerSamplesArray = 22,
  PbExerciseSamples_FieldNumber_LeftPedalPowerOfflineArray = 23,
  PbExerciseSamples_FieldNumber_RightPedalPowerSamplesArray = 24,
  PbExerciseSamples_FieldNumber_RightPedalPowerOfflineArray = 25,
  PbExerciseSamples_FieldNumber_LeftPowerCalibrationArray = 26,
  PbExerciseSamples_FieldNumber_RightPowerCalibrationArray = 27,
  PbExerciseSamples_FieldNumber_RrSamples = 28,
  PbExerciseSamples_FieldNumber_ExerciseIntervalledSampleListArray = 29,
  PbExerciseSamples_FieldNumber_PauseTimesArray = 30,
};

/**
 *
 * Sample data of the exercise.
 * ! All fields are required.
 **/
@interface PbExerciseSamples : GPBMessage

/**
 * Default recording interval of samples
 * Note: Samples listed in the root of the PbExerciseSamples will use this interval
 * Note: Samples in the exercise_intervalled_sample_list will use this interval unless
 *       otherwise specified inside the exercise_intervalled_sample_list
 **/
@property(nonatomic, readwrite, strong, null_resettable) PbDuration *recordingInterval;
/** Test to see if @c recordingInterval has been set. */
@property(nonatomic, readwrite) BOOL hasRecordingInterval;

/** heart rate samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBUInt32Array *heartRateSamplesArray;
/** The number of items in @c heartRateSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger heartRateSamplesArray_Count;

/** indicate start and stop indexes when there has not been connection to heart rate sensor */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *heartRateOfflineArray;
/** The number of items in @c heartRateOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger heartRateOfflineArray_Count;

/** cadence samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBUInt32Array *cadenceSamplesArray;
/** The number of items in @c cadenceSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger cadenceSamplesArray_Count;

/** indicate start and stop indexes when there has not been connection to a sensor providing cadence */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *cadenceOfflineArray;
/** The number of items in @c cadenceOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger cadenceOfflineArray_Count;

/**
 * altitude samples
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *altitudeSamplesArray;
/** The number of items in @c altitudeSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger altitudeSamplesArray_Count;

/** Altitude sensor offline status */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *altitudeOfflineArray;
/** The number of items in @c altitudeOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger altitudeOfflineArray_Count;

/** indicate start and stop indexes, used calibration value and operation type of calibration */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *altitudeCalibrationArray;
/** The number of items in @c altitudeCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger altitudeCalibrationArray_Count;

/** temperature samples */
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *temperatureSamplesArray;
/** The number of items in @c temperatureSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger temperatureSamplesArray_Count;

/** Temperature sensor offline status */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *temperatureOfflineArray;
/** The number of items in @c temperatureOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger temperatureOfflineArray_Count;

/**
 * speed samples
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *speedSamplesArray;
/** The number of items in @c speedSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger speedSamplesArray_Count;

/** indicate start and stop indexes when there has not been connection to speed sensor */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *speedOfflineArray;
/** The number of items in @c speedOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger speedOfflineArray_Count;

/**
 * distance samples: total distance from the beginning of the exercise
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *distanceSamplesArray;
/** The number of items in @c distanceSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger distanceSamplesArray_Count;

/** indicate start and stop indexes when there has not been connection to distance measure sensor */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *distanceOfflineArray;
/** The number of items in @c distanceOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger distanceOfflineArray_Count;

/**
 * stride length samples
 * Note: Samples are already calibrated samples
 **/
@property(nonatomic, readwrite, strong, null_resettable) GPBUInt32Array *strideLengthSamplesArray;
/** The number of items in @c strideLengthSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger strideLengthSamplesArray_Count;

/** indicate start and stop indexes when there has not been connection to stride sensor */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *strideLengthOfflineArray;
/** The number of items in @c strideLengthOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger strideLengthOfflineArray_Count;

/** indicate the information of the stride sensor calibration */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *strideCalibrationArray;
/** The number of items in @c strideCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger strideCalibrationArray_Count;

/** User 1d acceleration samples as m/s2 */
@property(nonatomic, readwrite, strong, null_resettable) GPBFloatArray *forwardAccelerationArray;
/** The number of items in @c forwardAccelerationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger forwardAccelerationArray_Count;

/** indicate start and stop indexes when there has not been connection to sensor that produces forward acceleration */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *forwardAccelerationOfflineArray;
/** The number of items in @c forwardAccelerationOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger forwardAccelerationOfflineArray_Count;

/** User walking/running/standing status samples */
// |movingTypeSamplesArray| contains |PbMovingType|
@property(nonatomic, readwrite, strong, null_resettable) GPBEnumArray *movingTypeSamplesArray;
/** The number of items in @c movingTypeSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger movingTypeSamplesArray_Count;

/** indicate start and stop indexes when there has not been connection to sensor that produces moving_type */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *movingTypeOfflineArray;
/** The number of items in @c movingTypeOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger movingTypeOfflineArray_Count;

/** Crank based power samples from left pedal */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbPowerMeasurements*> *leftPedalPowerSamplesArray;
/** The number of items in @c leftPedalPowerSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger leftPedalPowerSamplesArray_Count;

/** Indicate start and stop indexes when there has not been connection to sensor that produces left_pedal_power_samples */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *leftPedalPowerOfflineArray;
/** The number of items in @c leftPedalPowerOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger leftPedalPowerOfflineArray_Count;

/** Crank based power samples from right pedal */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbPowerMeasurements*> *rightPedalPowerSamplesArray;
/** The number of items in @c rightPedalPowerSamplesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger rightPedalPowerSamplesArray_Count;

/** Indicate start and stop indexes when there has not been connection to sensor that produces right_pedal_power_samples */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbSensorOffline*> *rightPedalPowerOfflineArray;
/** The number of items in @c rightPedalPowerOfflineArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger rightPedalPowerOfflineArray_Count;

/**
 * Indicate the information of the bike power sensor calibration
 * This field is most likely used only for testing purposes
 **/
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *leftPowerCalibrationArray;
/** The number of items in @c leftPowerCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger leftPowerCalibrationArray_Count;

/**
 * Indicate the information of the bike power sensor calibration
 * This field is most likely used only for testing purposes
 **/
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbCalibrationValue*> *rightPowerCalibrationArray;
/** The number of items in @c rightPowerCalibrationArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger rightPowerCalibrationArray_Count;

/** Heart Rate R-R sample data. */
@property(nonatomic, readwrite, strong, null_resettable) PbExerciseRRIntervals *rrSamples;
/** Test to see if @c rrSamples has been set. */
@property(nonatomic, readwrite) BOOL hasRrSamples;

/** Exercise samples with sample type specific recording intervals */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbExerciseIntervalledSampleList*> *exerciseIntervalledSampleListArray;
/** The number of items in @c exerciseIntervalledSampleListArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger exerciseIntervalledSampleListArray_Count;

/** Exercise pause times */
@property(nonatomic, readwrite, strong, null_resettable) NSMutableArray<PbPauseTime*> *pauseTimesArray;
/** The number of items in @c pauseTimesArray without causing the array to be created. */
@property(nonatomic, readonly) NSUInteger pauseTimesArray_Count;

@end

NS_ASSUME_NONNULL_END

CF_EXTERN_C_END

#pragma clang diagnostic pop

// @@protoc_insertion_point(global_scope)
