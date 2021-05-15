package com.reactnativepolarblesdk;

import android.content.Context;
// import android.support.annotation.Nullable;
import androidx.annotation.Nullable;
import android.bluetooth.BluetoothAdapter;

import java.util.UUID;
import java.util.HashMap;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.errors.PolarInvalidArgument;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarSensorSetting;
import polar.com.sdk.api.model.PolarAccelerometerData;
import polar.com.sdk.api.model.PolarAccelerometerData.PolarAccelerometerSample;
import polar.com.sdk.api.model.PolarEcgData;
import polar.com.sdk.api.model.PolarHrData;
import polar.com.sdk.api.model.PolarOhrPPGData;
import polar.com.sdk.api.model.PolarOhrPPGData.PolarOhrPPGSample;
import polar.com.sdk.api.model.PolarOhrPPIData;
import polar.com.sdk.api.model.PolarOhrPPIData.PolarOhrPPISample;
import polar.com.sdk.api.model.PolarExerciseEntry;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;


public class PolarBleSdkModuleModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;

    public PolarBleApi api;

    // todo make hashmap of disposables with polar device id as key to manage multiple devices
    // (provided the sdk allows it)
    // e.g. :
    // private Map<String, Disposable> polarDevices = new HashMap<String, Disposable>();
    // then we will be able to call startEcgStreaming (and stopEcgStreaming) with a deviceId argument
    // (and not a useless callback)
    // for now we will keep a plugin for a single simultaneously connected sensor.
    // All events are emitted with a "id": deviceId field, so the api won't change much
    // when we add multiple sensor ability

    //*
    private class Device {
        // private String deviceId = "";
        public Boolean hrReady = false;
        public Boolean ecgReady = false;
        public Boolean accReady = false;
        public Boolean ppgReady = false;
        public Boolean ppiReady = false;
        public Disposable broadcastDisposable = null;
        public Disposable ecgDisposable = null;
        public Disposable accDisposable = null;
        public Disposable ppgDisposable = null;
        public Disposable ppiDisposable = null;
        public Disposable scanDisposable = null;
        public Disposable autoConnectDisposable = null;
        public PolarExerciseEntry exerciseEntry = null;
        // private String connectionState = "disconnected";

        // public Device() {}

        // public Device(deviceId) {
        //     this->deviceId = deviceId;
        // }
    }
    //*/

    private HashMap<String, Device> devices = new HashMap<>();

    public String deviceId = "";
    private Disposable broadcastDisposable = null;
    private Disposable ecgDisposable = null;
    private Disposable accDisposable = null;
    private Disposable ppgDisposable = null;
    private Disposable ppiDisposable = null;
    private Disposable scanDisposable = null;
    private Disposable autoConnectDisposable = null;
    private PolarExerciseEntry exerciseEntry = null;

    private ReactApplicationContext ctx;

    public PolarBleSdkModuleModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        ctx = reactContext;

        api = PolarBleApiDefaultImpl.defaultImplementation(reactContext,
                PolarBleApi.FEATURE_POLAR_SENSOR_STREAMING |
                        PolarBleApi.FEATURE_BATTERY_INFO |
                        PolarBleApi.FEATURE_DEVICE_INFO |
                        PolarBleApi.FEATURE_HR);

        api.setApiCallback(new PolarBleApiCallback() {
            @Override
            public void blePowerStateChanged(boolean powered) {
                WritableMap params = Arguments.createMap();
                params.putString("id", deviceId);
                params.putBoolean("state", powered);
                // emit(ctx, "blePower", params);
            }

            /* * * * * * * * * * * * DEVICE CONNECTION * * * * * * * * * * * */

            @Override
            public void deviceConnected(PolarDeviceInfo deviceInfo) {
                String id = deviceInfo.deviceId;
                devices.put(id, new Device());
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                params.putString("state", "connected");
                emit(ctx, "connectionState", params);
                // set deviceId to deviceInfo.deviceId ?
            }

            @Override
            public void deviceConnecting(PolarDeviceInfo deviceInfo) {
                WritableMap params = Arguments.createMap();
                params.putString("id", deviceInfo.deviceId);
                params.putString("state", "connecting");
                emit(ctx, "connectionState", params);
            }

            @Override
            public void deviceDisconnected(PolarDeviceInfo deviceInfo) {
                String id = deviceInfo.deviceId;
                devices.remove(id);
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                params.putString("state", "disconnected");
                emit(ctx, "connectionState", params);
                // set deviceId to null ?
            }

            /* * * * * * * * * * * * * FEATURES READY * * * * * * * * * * * * */

            @Override
            public void hrFeatureReady(String id) {
                Device d = devices.get(id);
                d.hrReady = true;
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                emit(ctx, "hrFeatureReady", params);
            }

            @Override
            public void ecgFeatureReady(String id) {
                Device d = devices.get(id);
                d.ecgReady = true;
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                emit(ctx, "ecgFeatureReady", params);
            }

            @Override
            public void accelerometerFeatureReady(String id) {
                Device d = devices.get(id);
                d.accReady = true;
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                emit(ctx, "accelerometerFeatureReady", params);
            }

            @Override
            public void ppgFeatureReady(String id) {
                Device d = devices.get(id);
                d.ppgReady = true;
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                emit(ctx, "ppgFeatureReady", params);
            }

            @Override
            public void ppiFeatureReady(String id) {
                Device d = devices.get(id);
                d.ppiReady = true;
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                emit(ctx, "ppiFeatureReady", params);
            }

            @Override
            public void biozFeatureReady(String id) {
                // TODO (wth is bioz ?)
            }

            @Override
            public void disInformationReceived(String id, UUID u, String value) {
                if (u.equals(UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb"))) {
                    WritableMap params = Arguments.createMap();
                    params.putString("id", id);
                    params.putString("value", value.trim());
                    emit(ctx, "firmwareVersion", params);
                } else {
                    WritableMap params = Arguments.createMap();
                    params.putString("id", id);
                    params.putString("uuid", u.toString());
                    params.putString("value", value);
                    emit(ctx, "disInformation", params);
                }
            }

            @Override
            public void batteryLevelReceived(String id, int level) {
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                params.putInt("value", level);
                emit(ctx, "batteryLevel", params);
            }

            @Override
            public void hrNotificationReceived(String id,
                                               PolarHrData hrData) {
                WritableMap params = Arguments.createMap();
                params.putString("id", id);
                params.putInt("hr", hrData.hr);
                params.putBoolean("contact", hrData.contactStatus);
                params.putBoolean("contactSupported", hrData.contactStatusSupported);
                params.putBoolean("rrAvailable", hrData.rrAvailable);

                WritableArray rrs = Arguments.createArray();
                for (Integer i : hrData.rrs) { rrs.pushInt(i); }
                params.putArray("rrs", rrs);

                WritableArray rrsMs = Arguments.createArray();
                for (Integer i : hrData.rrsMs) { rrsMs.pushInt(i); }
                params.putArray("rrsMs", rrsMs);

                emit(ctx, "hrData", params);
            }

            @Override
            public void polarFtpFeatureReady(String id) {
                // TODO (wth is polar ftp ? has something to do with exercise entry ?)
            }
        });
    }

    @Override
    public String getName() {
        return "PolarBleSdkModule";
    }

    /*
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("truc", 1);
        return constants;
    }
    //*/

    @Override
    public void onHostPause() {
        api.backgroundEntered();
    }

    @Override
    public void onHostResume() {
        api.foregroundEntered();
    }

    @Override
    public void onHostDestroy() {
        api.shutDown();
    }

    private void emit(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        // guard found in some old SO posts
        // (not always necessary but avoids random initialization order bugs)
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
        }
    }

    @ReactMethod
    public void connectToDevice(String id) {
        // this.deviceId = id;
        WritableMap params = Arguments.createMap();
        params.putString("id", id);
        params.putString("state", "scanning");
        emit(reactContext, "connectionState", params);
        try {
            // api.connectToDevice(this.deviceId);
            api.connectToDevice(id);
        } catch (Exception e) {

        }
    }

    @ReactMethod
    public void disconnectFromDevice(String id) {
        // this.deviceId = id;
        WritableMap params = Arguments.createMap();
        params.putString("id", id);
        params.putString("state", "disconnecting");
        emit(reactContext, "connectionState", params);
        try {
            // api.disconnectFromDevice(this.deviceId);
            api.disconnectFromDevice(id);
        } catch (Exception e) {
            params.putString("state", "disconnected");
            emit(reactContext, "connectionState", params);
        }
    }

    @ReactMethod
    public void startEcgStreaming(String id) {
        if (!devices.containsKey(id)) return;

        final String identifier = id;
        final Device d = devices.get(id);

        if (d.ecgReady && d.ecgDisposable == null) {
            /*
            ecgDisposable = api.requestEcgSettings(identifier).toFlowable().flatMap(
                (Function<PolarSensorSetting, Publisher<PolarEcgData>>) setting -> {
                    return api.startEcgStreaming(identifier, setting.maxSettings());
                }
            ).subscribe(
                ecgData -> {
                    WritableMap params = Arguments.createMap();
                    WritableArray samples = Arguments.createArray();
                    for (Integer s : ecgData.samples) {
                        samples.pushInt(s);
                    }
                    params.putString("id", identifier);
                    params.putArray("samples", samples);
                    params.putDouble("timeStamp", new Double(ecgData.timeStamp));
                    emit(reactContext, "ecgData", params);
                },
                throwable -> {
                    // Log.e(TAG, "" + throwable.toString())
                    ecgDisposable = null;
                },
                () -> {
                    // Log.d(TAG, "complete")
                }
            );
            //*/

            //*
            d.ecgDisposable =
                api.requestEcgSettings(id).toFlowable().flatMap(
                    new Function<PolarSensorSetting, Publisher<PolarEcgData>>() {
                        @Override
                        public Publisher<PolarEcgData> apply(PolarSensorSetting sensorSetting) throws Exception {
                            return api.startEcgStreaming(identifier, sensorSetting.maxSettings());
                        }
                    }
                ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new Consumer<PolarEcgData>() {
                        @Override
                        public void accept(PolarEcgData polarEcgData) throws Exception {
                            WritableMap params = Arguments.createMap();
                            WritableArray samples = Arguments.createArray();
                            for (Integer s : polarEcgData.samples) {
                                samples.pushInt(s);
                            }
                            params.putString("id", identifier);
                            params.putArray("samples", samples);
                            params.putDouble("timeStamp", new Double(polarEcgData.timeStamp));
                            emit(reactContext, "ecgData", params);
                        }
                    },
                    new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            // Log.e(TAG, "" + throwable.getLocalizedMessage());
                            d.ecgDisposable = null;
                        }
                    },
                    new Action() {
                        @Override
                        public void run() throws Exception {
                            // Log.d(TAG, "complete");
                        }
                    }
                );
            //*/

            // streamCallback.invoke("ecg started");
        }        
    }

    @ReactMethod
    public void stopEcgStreaming(String id) {
        if (!devices.containsKey(id)) return;

        Device d = devices.get(id);
        if (d.ecgDisposable != null) {
            d.ecgDisposable.dispose();
            d.ecgDisposable = null;
        }
    }

    @ReactMethod
    public void startAccStreaming(String id) {
        if (!devices.containsKey(id)) return;

        final String identifier = id;
        final Device d = devices.get(id);        
        if (d.accReady && d.accDisposable == null) {
            /*
            accDisposable = api.requestAccSettings(identifier).toFlowable().flatMap(
                (Function<PolarSensorSetting, Publisher<PolarAccelerometerData>>) settings -> {
                    return api.startAccStreaming(identifier, settings.maxSettings());
                }
            ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                accData -> {
                    WritableMap params = Arguments.createMap();
                    WritableArray samples = Arguments.createArray();
                    for (PolarAccelerometerSample sample : accData.samples) {
                        WritableArray vector = Arguments.createArray();
                        vector.pushInt(sample.x);
                        vector.pushInt(sample.y);
                        vector.pushInt(sample.z);
                        samples.pushArray(vector);
                    }
                    params.putString("id", identifier);
                    params.putArray("samples", samples);
                    params.putDouble("timeStamp", new Double(accData.timeStamp));
                    emit(reactContext, "accData", params);
                },
                throwable -> {
                    // Log.e(TAG,"" + throwable.getLocalizedMessage());
                    accDisposable = null;
                },
                () -> {
                    // Log.d(TAG,"complete");
                }
            );
            //*/

            //*
            d.accDisposable = api.requestAccSettings(id).toFlowable().flatMap(
                new Function<PolarSensorSetting, Publisher<PolarAccelerometerData>>() {
                    @Override
                    public Publisher<PolarAccelerometerData> apply(PolarSensorSetting sensorSetting) throws Exception {
                        return api.startAccStreaming(identifier, sensorSetting.maxSettings());
                    }
                }
            ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<PolarAccelerometerData>() {
                    @Override
                    public void accept(PolarAccelerometerData polarAccData) throws Exception {
                        WritableMap params = Arguments.createMap();
                        WritableArray samples = Arguments.createArray();
                        for (PolarAccelerometerSample sample : polarAccData.samples) {
                            WritableMap vector = Arguments.createMap();
                            vector.putInt("x", sample.x);
                            vector.putInt("y", sample.y);
                            vector.putInt("z", sample.z);
                            samples.pushMap(vector);
                        }
                        params.putString("id", identifier);
                        params.putArray("samples", samples);
                        params.putDouble("timeStamp", new Double(polarAccData.timeStamp));
                        // params.putString("connectionState", "disconnected");
                        emit(reactContext, "accData", params);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // Log.e(TAG,
                        //         "" + throwable.getLocalizedMessage());
                        d.accDisposable = null;
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        // Log.d(TAG, "complete");
                    }
                }
            );
            //*/

            // streamCallback.invoke("acc started");
        }
    }

    @ReactMethod
    public void stopAccStreaming(String id) {
        if (!devices.containsKey(id)) return;

        Device d = devices.get(id);        
        if (d.accDisposable != null) {
            d.accDisposable.dispose();
            d.accDisposable = null;
        }
    }

    @ReactMethod
    public void startPpgStreaming(String id) {
        if (!devices.containsKey(id)) return;

        final String identifier = id;
        final Device d = devices.get(id);
        if (d.ppgReady && d.ppgDisposable == null) {
            /*
            ppgDisposable = api.requestPpgSettings(identifier).toFlowable().flatMap(
                (Function<PolarSensorSetting, Publisher<PolarOhrPPGData>>) setting -> {
                    api.startOhrPPGStreaming(identifier, setting.maxSettings());
                }
            ).subscribe(
                ppgData -> {
                    WritableMap params = Arguments.createMap();
                    WritableArray samples = Arguments.createArray();
                    for (PolarOhrPPGSample sample : ppgData.samples) {
                        WritableMap vector = Arguments.createMap();
                        vector.putInt("ppg0", sample.ppg0);
                        vector.putInt("ppg1", sample.ppg1);
                        vector.putInt("ppg2", sample.ppg2);
                        vector.putInt("ambient", sample.ambient);
                        samples.pushMap(vector);
                    }
                    params.putString("id", identifier);
                    params.putArray("samples", samples);
                    params.putDouble("timeStamp", new Double(ppgData.timeStamp));
                    emit(reactContext, "ppgData", params);
                },
                throwable -> {
                    // Log.e(TAG,""+throwable.getLocalizedMessage())
                    ppgDisposable = null;
                },
                () -> {
                    // Log.d(TAG,"complete")
                }
            );
            //*/

            d.ppgDisposable = api.requestPpgSettings(id).toFlowable().flatMap(
                new Function<PolarSensorSetting, Publisher<PolarOhrPPGData>>() {
                    @Override
                    public Publisher<PolarOhrPPGData> apply(PolarSensorSetting sensorSetting) throws Exception {
                        return api.startOhrPPGStreaming(identifier, sensorSetting.maxSettings());
                    }
                }
            ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<PolarOhrPPGData>() {
                    @Override
                    public void accept(PolarOhrPPGData polarPpgData) throws Exception {
                        WritableMap params = Arguments.createMap();
                        WritableArray samples = Arguments.createArray();
                        for (PolarOhrPPGSample sample : polarPpgData.samples) {
                            WritableMap vector = Arguments.createMap();
                            vector.putInt("ppg0", sample.ppg0);
                            vector.putInt("ppg1", sample.ppg1);
                            vector.putInt("ppg2", sample.ppg2);
                            vector.putInt("ambient", sample.ambient);
                            samples.pushMap(vector);
                        }
                        params.putString("id", identifier);
                        params.putArray("samples", samples);
                        params.putDouble("timeStamp", new Double(polarPpgData.timeStamp));
                        emit(reactContext, "ppgData", params);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // Log.e(TAG,
                        //         "" + throwable.getLocalizedMessage());
                        d.ppgDisposable = null;
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        // Log.d(TAG, "complete");
                    }
                }
            );
        }
    }

    @ReactMethod
    public void stopPpgStreaming(String id) {
        if (!devices.containsKey(id)) return;

        Device d = devices.get(id);        
        if (d.ppgDisposable != null) {
            d.ppgDisposable.dispose();
            d.ppgDisposable = null;
        }
    }

    @ReactMethod
    public void startPpiStreaming(String id) {
        if (!devices.containsKey(id)) return;

        final String identifier = id;
        final Device d = devices.get(id);        
        if(d.ppiReady && d.ppiDisposable == null) {
            /*
            ppiDisposable = api.startOhrPPIStreaming(identifier).observeOn(AndroidSchedulers.mainThread()).subscribe(
                ppiData -> {
                    WritableMap params = Arguments.createMap();
                    WritableArray samples = Arguments.createArray();
                    for (PolarOhrPPISample sample : ppiData.samples) {
                        WritableArray vector = Arguments.createMap();
                        vector.putInt("hr", sample.hr);
                        vector.putInt("ppInMs", sample.ppi);
                        vector.putInt("ppErrorEstimate", sample.errorEstimate);
                        vector.putBoolean("blockerBit", sample.blockerBit);
                        vector.putBoolean("skinContactStatus", sample.skinContactStatus);
                        vector.putBoolean("skinContactSupported", sample.skinContactSupported);
                        samples.pushMap(vector);
                    }
                    params.putString("id", identifier);
                    params.putArray("samples", samples);
                    params.putDouble("timeStamp", new Double(ppiData.timeStamp));
                    emit(reactContext, "ppiData", params);
                },
                throwable -> {
                    // Log.e(TAG,""+throwable.getLocalizedMessage());
                    ppgDisposable = null;
                },
                () -> {
                    // Log.d(TAG,"complete");
                }
            );
            //*/

            d.ppiDisposable = api.startOhrPPIStreaming(id).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<PolarOhrPPIData>() {
                    @Override
                    public void accept(PolarOhrPPIData polarPpiData) throws Exception {
                        WritableMap params = Arguments.createMap();
                        WritableArray samples = Arguments.createArray();
                        for (PolarOhrPPISample sample : polarPpiData.samples) {
                            WritableMap vector = Arguments.createMap();
                            vector.putInt("hr", sample.hr);
                            vector.putInt("ppInMs", sample.ppi);
                            vector.putInt("ppErrorEstimate", sample.errorEstimate);
                            vector.putBoolean("blockerBit", sample.blockerBit);
                            vector.putBoolean("skinContactStatus", sample.skinContactStatus);
                            vector.putBoolean("skinContactSupported", sample.skinContactSupported);
                            samples.pushMap(vector);
                        }
                        params.putString("id", identifier);
                        params.putArray("samples", samples);
                        params.putDouble("timeStamp", new Double(polarPpiData.timeStamp));
                        emit(reactContext, "ppiData", params);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // Log.e(TAG,
                        //         "" + throwable.getLocalizedMessage());
                        d.ppiDisposable = null;
                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        // Log.d(TAG, "complete");
                    }
                }
            );
        }
    }

    @ReactMethod
    public void stopPpiStreaming(String id) {
        if (!devices.containsKey(id)) return;

        Device d = devices.get(id);        
        if (d.ppiDisposable != null) {
            d.ppiDisposable.dispose();
            d.ppiDisposable = null;
        }
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void printSomething(Callback callback) {
        // return "something";
        callback.invoke("something");
    }
}
