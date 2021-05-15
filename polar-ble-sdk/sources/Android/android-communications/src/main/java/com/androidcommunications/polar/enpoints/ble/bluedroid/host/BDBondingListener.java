package com.androidcommunications.polar.enpoints.ble.bluedroid.host;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.androidcommunications.polar.api.ble.BleLogger;
import com.androidcommunications.polar.common.ble.AtomicSet;

class BDBondingListener {

    interface AuthenticationObserverInterface {
        // bonding completion
        void bonding();

        void bonded();

        void bondNone();
    }

    private final static String TAG = BDBondingListener.class.getSimpleName();
    private Context context;
    private AtomicSet<BondingObserver> authenticationObservers = new AtomicSet<>();

    BDBondingListener(final Context context) {
        this.context = context;
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        context.registerReceiver(mReceiver, intent);
    }

    void stopBroadcastReceiver() {
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    static abstract class BondingObserver implements AuthenticationObserverInterface {
        private BluetoothDevice device;

        BondingObserver(BluetoothDevice device) {
            this.device = device;
        }

        public BluetoothDevice getDevice() {
            return device;
        }
    }

    void addObserver(BondingObserver observer) {
        authenticationObservers.add(observer);
    }

    void removeObserver(BondingObserver observer) {
        authenticationObservers.remove(observer);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && action != null) {
                if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    BleLogger.d(TAG, "Bond manager state:" + state + " action: " + intent.toString());
                    switch (state) {
                        case BluetoothDevice.BOND_BONDING:
                            authenticationObservers.accessAll(object -> {
                                if (object.getDevice().equals(device)) {
                                    object.bonding();
                                }
                            });
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            authenticationObservers.accessAll(object -> {
                                if (object.getDevice().equals(device)) {
                                    object.bonded();
                                }
                            });
                            break;
                        case BluetoothDevice.BOND_NONE:
                            authenticationObservers.accessAll(object -> {
                                if (object.getDevice().equals(device)) {
                                    object.bondNone();
                                }
                            });
                            break;
                    }
                }
            }
        }
    };
}
