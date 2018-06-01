package com.aphrodite.cloudweather.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.config.HttpConfig;
import com.aphrodite.cloudweather.http.OkHttpUtils;
import com.aphrodite.cloudweather.utils.Logger;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aphrodite.cloudweather.config.HttpConfig.ALIYUN_HOST;
import static com.aphrodite.cloudweather.config.HttpConfig.APP_CODE;
import static com.aphrodite.cloudweather.config.HttpConfig.RequestParameters.CITY_ID_KEY;

public class CloudWeatherActivity extends AppCompatActivity {
    private static final String TAG = CloudWeatherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_weather);
        queryWeather("219");
        queryCities();
    }

    /**
     * 根据cityID查询天气情况
     *
     * @param cityId
     */
    private void queryWeather(String cityId) {
        Map<String, String> header = new HashMap<String, String>();
        header.put(HttpConfig.Header.AUTHORIZATION_KEY, "APPCODE " + APP_CODE);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put(CITY_ID_KEY, cityId);
        try {
            String url = OkHttpUtils.getInstance().buildUrl(ALIYUN_HOST, HttpConfig.QueryWeather.PATH, querys);
            OkHttpUtils.getInstance().doGet(url, header, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Enter queryWeather method." + response.body().string());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询城市
     */
    private void queryCities() {
        Map<String, String> header = new HashMap<String, String>();
        header.put(HttpConfig.Header.AUTHORIZATION_KEY, "APPCODE " + APP_CODE);
        try {
            String url = OkHttpUtils.getInstance().buildUrl(ALIYUN_HOST, HttpConfig.QueryCities.PATH, null);
            OkHttpUtils.getInstance().doGet(url, header, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Enter queryCities method." + response.body().string());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
