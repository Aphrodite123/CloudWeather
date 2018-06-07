package com.aphrodite.cloudweather.http.response;

import com.aphrodite.cloudweather.http.base.BaseResponse;
import com.aphrodite.cloudweather.http.bean.LocationBean;

/**
 * Created by Aphrodite on 2018/6/7.
 */
public class QueryLocationResponse extends BaseResponse {
    private LocationBean result;

    public LocationBean getResult() {
        return result;
    }

    public void setResult(LocationBean result) {
        this.result = result;
    }

}
