package com.aphrodite.cloudweather.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by Aphrodite on 2018/6/1.
 */
@Entity
public class CityEntity implements Serializable {
    private static final long serialVersionUID = 7329056468875031975L;

    @Property(nameInDb = "city_id")
    private String cityid;

    @Property(nameInDb = "parent_id")
    private String parentid;

    @Property(nameInDb = "city_code")
    private String citycode;

    @Property(nameInDb = "city")
    private String city;

    @Generated(hash = 2001321047)
    public CityEntity() {
    }

    @Generated(hash = 433702631)
    public CityEntity(String cityid, String parentid, String citycode, String city) {
        this.cityid = cityid;
        this.parentid = parentid;
        this.citycode = citycode;
        this.city = city;
    }

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
