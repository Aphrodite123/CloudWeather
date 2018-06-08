package com.aphrodite.cloudweather.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.application.CloudWeatherApplication;
import com.aphrodite.cloudweather.database.entity.CityEntity;
import com.aphrodite.cloudweather.presenter.LocationImpl;
import com.aphrodite.cloudweather.presenter.OkHttpRequestImpl;
import com.aphrodite.cloudweather.ui.base.BaseActivity;
import com.aphrodite.cloudweather.utils.Logger;
import com.aphrodite.cloudweather.view.inter.ILocation;
import com.aphrodite.cloudweather.view.inter.IQueryWeather;
import com.usher.greendao.greendao.gen.CityEntityDao;
import com.usher.greendao.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import okhttp3.Response;

public class CloudWeatherActivity extends BaseActivity {
    private static final String TAG = CloudWeatherActivity.class.getSimpleName();

    private final static int REQUEST_PERMISSION = 0x121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_cloud_weather;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        OkHttpRequestImpl.getInstance().queryCities(new IQueryWeather() {
            @Override
            public void onFailed() {

            }

            @Override
            public void onSuccess(Response response) {
                DaoSession daoSession = CloudWeatherApplication.getDaoManager().getDaoSession();
                if (null == daoSession) {
                    return;
                }
                CityEntityDao cityEntityDao = daoSession.getCityEntityDao();
                QueryBuilder builder = cityEntityDao.queryBuilder();
                builder.where(CityEntityDao.Properties.Parentid.eq(0));
                List<CityEntity> entities = builder.list();
            }
        });

        if (!hasPermission()) {
            requestPermission();
        } else {
            queryLocation();
        }
    }

    private void queryLocation() {
        LocationImpl location = new LocationImpl(new ILocation() {
            @Override
            public void onLocationFailed() {

            }

            @Override
            public void onLocationSuccess(List<Address> addresses) {
                Logger.i(TAG, "Enter location method." + addresses);
            }
        });
        location.startLocation();
    }

    @Override
    protected boolean hasPermission() {
        return Build.VERSION.SDK_INT < 23
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void requestPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    queryLocation();
                } else {
                    Logger.i(TAG, "Enter onRequestPermissionsResult method. Request permission failed!");
                }
        }
    }

}
