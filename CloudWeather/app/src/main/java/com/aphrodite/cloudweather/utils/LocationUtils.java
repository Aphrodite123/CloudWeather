package com.aphrodite.cloudweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.aphrodite.cloudweather.application.CloudWeatherApplication;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public class LocationUtils {
    private static final String TAg = LocationUtils.class.getSimpleName();
    private static LocationUtils mLocationUtils = null;

    public LocationUtils() {
    }

    public static LocationUtils getInstance() {
        if (null == mLocationUtils) {
            synchronized (LocationUtils.class) {
                if (null == mLocationUtils) {
                    mLocationUtils = new LocationUtils();
                }
            }
        }
        return mLocationUtils;
    }

    /**
     * network获取定位方式
     */
    public Location getNetWorkLocation() {
        Location location = null;
        LocationManager manager = getLocationManager(CloudWeatherApplication.getApp());

        if (ActivityCompat.checkSelfPermission(CloudWeatherApplication.getApp(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CloudWeatherApplication.getApp(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        //是否支持Network定位
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //获取最后的network定位信息
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    private LocationManager getLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

}
