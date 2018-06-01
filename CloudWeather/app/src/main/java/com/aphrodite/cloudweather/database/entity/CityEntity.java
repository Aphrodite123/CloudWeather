package com.aphrodite.cloudweather.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Aphrodite on 2018/6/1.
 */
@Entity
public class CityEntity {
    /**
     * id
     */
    @Id(autoincrement = true)//ID 表示标识主键 且主键不能用int autoincrement = true 表示主键会自增
    private long id;

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

    @Generated(hash = 972952575)
    public CityEntity(long id, String cityid, String parentid, String citycode, String city) {
        this.id = id;
        this.cityid = cityid;
        this.parentid = parentid;
        this.citycode = citycode;
        this.city = city;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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
