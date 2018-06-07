package com.aphrodite.cloudweather.presenter;

import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.aphrodite.cloudweather.application.CloudWeatherApplication;
import com.aphrodite.cloudweather.database.entity.LocationEntity;
import com.aphrodite.cloudweather.http.OkHttpUtils;
import com.aphrodite.cloudweather.http.bean.LocationBean;
import com.aphrodite.cloudweather.http.response.QueryLocationResponse;
import com.aphrodite.cloudweather.utils.LocationUtils;
import com.aphrodite.cloudweather.utils.Logger;
import com.aphrodite.cloudweather.view.inter.ILocation;
import com.google.gson.Gson;
import com.usher.greendao.greendao.gen.LocationEntityDao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.aphrodite.cloudweather.config.HttpConfig.BAIDU_APP_KEY;
import static com.aphrodite.cloudweather.config.HttpConfig.BAIDU_MAP_HOST;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public class LocationImpl {
    private static final String TAG = LocationImpl.class.getSimpleName();

    private ILocation mILocation = null;

    public LocationImpl(ILocation ilocation) {
        this.mILocation = ilocation;
    }

    public void startLocation() {
        LocationAsyncTask task = new LocationAsyncTask();
        task.execute();
    }

    private class LocationAsyncTask extends AsyncTask<Void, Void, Location> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Location doInBackground(Void... voids) {
            return LocationUtils.getInstance().getNetWorkLocation();
        }

        @Override
        protected void onPostExecute(Location location) {
            if (null == location) {
                Logger.i(TAG, "Enter onPostExecute method.Location failed!");
                return;
            }

            try {
                queryLocation(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据经纬度查询具体位置
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    private void queryLocation(String longitude, String latitude) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude)) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("output", "json");
        params.put("location", latitude + "," + longitude);
        params.put("key", BAIDU_APP_KEY);

        String url = OkHttpUtils.getInstance().buildUrl(BAIDU_MAP_HOST, null, params);
        OkHttpUtils.getInstance().doGet(url, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.i(TAG, "Enter queryLocation method.Location failed!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                processResult(response.body().string());
            }
        });
    }

    private void processResult(String response) {
        if (TextUtils.isEmpty(response)) {
            return;
        }

        Gson gson = new Gson();
        QueryLocationResponse locationResponse = gson.fromJson(response, QueryLocationResponse.class);
        if (null == locationResponse) {
            return;
        }
        LocationBean bean = locationResponse.getResult();

        LocationEntityDao dao = CloudWeatherApplication.getDaoManager().getDaoSession().getLocationEntityDao();
        LocationEntity entity = new LocationEntity();
        entity.setLng(bean.getLocation().getLng());
        entity.setLat(bean.getLocation().getLat());
        entity.setFormatted_address(bean.getFormatted_address());
        entity.setBusiness(bean.getBusiness());
        entity.setCityCode(bean.getCityCode());
        entity.setCity(bean.getAddressComponent().getCity());
        entity.setDirection(bean.getAddressComponent().getDirection());
        entity.setDistance(bean.getAddressComponent().getDistance());
        entity.setDistrict(bean.getAddressComponent().getDistrict());
        entity.setProvince(bean.getAddressComponent().getProvince());
        entity.setStreet(bean.getAddressComponent().getStreet());
        entity.setStreet_number(bean.getAddressComponent().getStreet_number());
        dao.deleteAll();
        dao.insert(entity);

        Logger.i(TAG, "Enter processResult method." + dao.queryBuilder().list());
    }

}
