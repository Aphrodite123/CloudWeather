package com.aphrodite.cloudweather.http.base;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public class BaseResponse {
    protected String status;
    protected String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
