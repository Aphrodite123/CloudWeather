package com.aphrodite.cloudweather.presenter;

import com.aphrodite.cloudweather.application.CloudWeatherApplication;
import com.aphrodite.cloudweather.config.HttpConfig;
import com.aphrodite.cloudweather.database.entity.CityEntity;
import com.aphrodite.cloudweather.http.OkHttpUtils;
import com.aphrodite.cloudweather.http.bean.CityBean;
import com.aphrodite.cloudweather.http.response.QueryCitiesResponse;
import com.aphrodite.cloudweather.utils.Logger;
import com.aphrodite.cloudweather.utils.ObjectUtils;
import com.aphrodite.cloudweather.view.inter.IQueryWeather;
import com.google.gson.Gson;
import com.usher.greendao.greendao.gen.CityEntityDao;
import com.usher.greendao.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.aphrodite.cloudweather.config.HttpConfig.ALIYUN_HOST;
import static com.aphrodite.cloudweather.config.HttpConfig.APP_CODE;
import static com.aphrodite.cloudweather.config.HttpConfig.RequestParameters.APP_CODE_KEY;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public class OkHttpRequestImpl {
    private static final String TAG = OkHttpRequestImpl.class.getSimpleName();
    private static OkHttpRequestImpl mOkHttpRequest = null;

    private Map<String, String> mHeaders = new HashMap<String, String>();

    public OkHttpRequestImpl() {
        initParams();
    }

    private void initParams() {
        mHeaders.put(HttpConfig.Header.AUTHORIZATION_KEY, APP_CODE_KEY + APP_CODE);
    }

    public static OkHttpRequestImpl getInstance() {
        if (null == mOkHttpRequest) {
            synchronized (OkHttpRequestImpl.class) {
                if (null == mOkHttpRequest) {
                    mOkHttpRequest = new OkHttpRequestImpl();
                }
            }
        }
        return mOkHttpRequest;
    }

    /**
     * 查询城市
     */
    public void queryCities(final IQueryWeather queryWeather) {
        try {
            String url = OkHttpUtils.getInstance().buildUrl(ALIYUN_HOST, HttpConfig.QueryCities.PATH, null);
            OkHttpUtils.getInstance().doGet(url, mHeaders, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Gson gson = new Gson();
                    final QueryCitiesResponse citiesResponse = gson.fromJson(response.body().string(), QueryCitiesResponse.class);
                    if (null != citiesResponse) {
                        DaoSession daoSession = CloudWeatherApplication.getDaoManager().getDaoSession();
                        if (null == daoSession) {
                            return;
                        }

                        final CityEntityDao cityEntityDao = daoSession.getCityEntityDao();
                        daoSession.runInTx(new Runnable() {
                            @Override
                            public void run() {
                                for (CityBean city : citiesResponse.getResult()) {
                                    if (null == city) {
                                        continue;
                                    }

                                    CityEntity cityEntity = new CityEntity();
                                    cityEntity.setCityid(city.getCityid());
                                    cityEntity.setParentid(city.getParentid());
                                    cityEntity.setCitycode(city.getCitycode());
                                    cityEntity.setCity(city.getCity());

                                    cityEntityDao.insertOrReplace(cityEntity);
                                }
                                if (null != queryWeather) {
                                    queryWeather.onSuccess(response);
                                }
                            }
                        });
                    }
                    if (null != queryWeather) {
                        queryWeather.onSuccess(response);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            Logger.w(TAG, "Enter queryCities method.UnsupportedEncodingException: " + e);
        }
    }

    /**
     * 根据条件查询天气
     *
     * @param params
     */
    public void queryWeather(Map<String, String> params, final IQueryWeather queryWeather) {
        if (ObjectUtils.isEmpty(params)) {
            return;
        }

        try {
            String url = OkHttpUtils.getInstance().buildUrl(ALIYUN_HOST, HttpConfig.QueryWeather.PATH, params);
            OkHttpUtils.getInstance().doGet(url, mHeaders, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Logger.i(TAG, "Enter queryWeather failed.IOException: " + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (null != queryWeather) {
                        queryWeather.onSuccess(response);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            Logger.w(TAG, "Enter queryWeather method.UnsupportedEncodingException: " + e);
        }
    }

}
