package com.aphrodite.cloudweather.http;

import com.aphrodite.cloudweather.utils.ObjectUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Aphrodite on 2018/6/1.
 */
public class OkHttpUtils {
    private static OkHttpUtils mOkHttpUtils = null;
    private OkHttpClient mOkHttpClient = null;

    public OkHttpUtils() {
        initOkHttp();
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //读取超时
        builder.readTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        //连接超时
        builder.connectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        //写入超时
        builder.writeTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        mOkHttpClient = builder.build();
    }

    public static OkHttpUtils getInstance() {
        if (null == mOkHttpUtils) {
            synchronized (OkHttpUtils.class) {
                if (null == mOkHttpUtils) {
                    mOkHttpUtils = new OkHttpUtils();
                }
            }
        }
        return mOkHttpUtils;
    }

    /**
     * 设置请求头
     *
     * @param params
     * @return
     */
    private Headers getHeaders(Map<String, String> params) {
        if (ObjectUtils.isEmpty(params)) {
            return null;
        }

        Headers headers = null;
        Headers.Builder builder = new Headers.Builder();

        Iterator<String> iterator = params.keySet().iterator();
        String key = "";
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            builder.add(key, params.get(key));
        }
        headers = builder.build();
        return headers;
    }

    /**
     * RequestBody Post请求
     *
     * @param params
     * @return
     */
    private RequestBody getRequestBody(Map<String, String> params) {
        if (ObjectUtils.isEmpty(params)) {
            return null;
        }

        RequestBody body = null;
        FormBody.Builder builder = new FormBody.Builder();

        Iterator<String> iterator = params.keySet().iterator();
        String key = "";
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            builder.add(key, params.get(key));
        }
        body = builder.build();
        return body;
    }

    public void doGet(String url, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(getHeaders(headers));
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void doPost(String url, Map<String, String> headers, Map<String, String> params, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(getHeaders(headers));
        builder.post(getRequestBody(params));
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }
        return sbUrl.toString();
    }

}
