package com.aphrodite.cloudweather.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.presenter.LocationImpl;
import com.aphrodite.cloudweather.ui.adapter.WeatherFragmentAdapter;
import com.aphrodite.cloudweather.ui.base.BaseActivity;
import com.aphrodite.cloudweather.ui.fragment.WeatherFragment;
import com.aphrodite.cloudweather.utils.DisplayUtils;
import com.aphrodite.cloudweather.utils.Logger;
import com.aphrodite.cloudweather.view.inter.ILocation;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CloudWeatherActivity extends BaseActivity {
    private static final String TAG = CloudWeatherActivity.class.getSimpleName();

    private final static int REQUEST_PERMISSION = 0x121;

    @BindView(R.id.weather_vp)
    ViewPager mViewPager;
    @BindView(R.id.wetaher_indicator)
    CirclePageIndicator mPageIndicator;
    @BindView(R.id.custom_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_ll)
    LinearLayout mSettingLinear;
    @BindView(R.id.location_iv)
    ImageView mLocationIV;
    @BindView(R.id.city_name_tv)
    TextView mCityName;
    @BindView(R.id.release_time_tv)
    TextView mReleaseTime;
    @BindView(R.id.cities_manage_ll)
    LinearLayout mCitiesManageLinear;

    private List<WeatherFragment> mFragmentList;
    private WeatherFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingToolbar();
    }

    private void settingToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
            params.topMargin = DisplayUtils.getStatusBarHeight(this);
            mToolbar.setLayoutParams(params);
        }
        mToolbar.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_cloud_weather;
    }

    @Override
    protected void initView() {
        mFragmentList = new ArrayList<>();

        WeatherFragment fragmentFirst = new WeatherFragment();
        mFragmentList.add(fragmentFirst);

        WeatherFragment fragmentSecond = new WeatherFragment();
        mFragmentList.add(fragmentSecond);

        WeatherFragment fragmentThird = new WeatherFragment();
        mFragmentList.add(fragmentThird);

        mFragmentAdapter = new WeatherFragmentAdapter(getSupportFragmentManager(), this, mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);

        mPageIndicator.setViewPager(mViewPager);
        mPageIndicator.setRadius(6);
    }

    @Override
    protected void initData() {
//        OkHttpRequestImpl.getInstance().queryCities(new IQueryWeather() {
//            @Override
//            public void onFailed() {
//
//            }
//
//            @Override
//            public void onSuccess(Response response) {
//                DaoSession daoSession = CloudWeatherApplication.getDaoManager().getDaoSession();
//                if (null == daoSession) {
//                    return;
//                }
//                CityEntityDao cityEntityDao = daoSession.getCityEntityDao();
//                QueryBuilder builder = cityEntityDao.queryBuilder();
//                builder.where(CityEntityDao.Properties.Parentid.eq(0));
//                List<CityEntity> entities = builder.list();
//            }
//        });
//
//        if (!hasPermission()) {
//            requestPermission();
//        } else {
//            queryLocation();
//        }
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
