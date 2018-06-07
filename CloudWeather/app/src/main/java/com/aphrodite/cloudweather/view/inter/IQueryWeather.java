package com.aphrodite.cloudweather.view.inter;

import okhttp3.Response;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public interface IQueryWeather {
    void onFailed();

    void onSuccess(Response response);
}
