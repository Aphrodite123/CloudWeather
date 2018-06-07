package com.aphrodite.cloudweather.http.bean;

import java.io.Serializable;

/**
 * Created by Aphrodite on 2018/6/7.
 */
public class CityBean implements Serializable {
    private static final long serialVersionUID = 7097465160115569562L;

    private String cityid;

    private String parentid;

    private String citycode;

    private String city;

    public String getCityid() {
        return this.cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getParentid() {
        return this.parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getCitycode() {
        return this.citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
