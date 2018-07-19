package com.aphrodite.cloudweather.presenter.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.aphrodite.cloudweather.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aphrodite on 2018/7/19.
 * 搜索WiFi列表信息
 */
public class WifiReceiver extends BroadcastReceiver {
    private static final String TAG = WifiReceiver.class.getSimpleName();
    private Context mContext;
    private List<ScanResult> mScanResults = new ArrayList<>();
    private WifiManager mWifiManager;

    public WifiReceiver(Context context) {
        this.mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
            mScanResults = mWifiManager.getScanResults();
            for (ScanResult scanResult : mScanResults) {
                Logger.i(TAG, "Enter onReceive method." + scanResult.toString() + "\n" + isMiDevice(scanResult));
            }

        }
    }

    private boolean isMiDevice(ScanResult scanResult) {
        return (scanResult.SSID.contains("_miap") || scanResult.SSID.contains("_mibt")) && scanResult.level > -70;
    }
}
