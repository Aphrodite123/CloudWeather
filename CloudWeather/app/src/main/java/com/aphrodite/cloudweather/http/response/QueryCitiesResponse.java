package com.aphrodite.cloudweather.http.response;

import com.aphrodite.cloudweather.http.base.BaseResponse;
import com.aphrodite.cloudweather.http.bean.CityBean;

import java.util.List;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public class QueryCitiesResponse extends BaseResponse {
    private List<CityBean> result;

    public List<CityBean> getResult() {
        return result;
    }

    public void setResult(List<CityBean> result) {
        this.result = result;
    }

}
