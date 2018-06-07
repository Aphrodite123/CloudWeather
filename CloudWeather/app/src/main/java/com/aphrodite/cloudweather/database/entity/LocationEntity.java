package com.aphrodite.cloudweather.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Aphrodite on 2018/6/6.
 */
@Entity
public class LocationEntity implements Serializable {
    private static final long serialVersionUID = -4624695195175995L;

    @Property(nameInDb = "longitude")
    private double lng;
    @Property(nameInDb = "latitude")
    private double lat;

    @Property(nameInDb = "formatted_address")
    private String formatted_address;

    @Property(nameInDb = "business")
    private String business;

    @Property(nameInDb = "city")
    private String city;

    @Property(nameInDb = "direction")
    private String direction;

    @Property(nameInDb = "distance")
    private String distance;

    @Property(nameInDb = "district")
    private String district;

    @Property(nameInDb = "province")
    private String province;

    @Property(nameInDb = "street")
    private String street;

    @Property(nameInDb = "street_number")
    private String street_number;

    @Property(nameInDb = "city_code")
    private int cityCode;

    @Generated(hash = 2075831581)
    public LocationEntity(double lng, double lat, String formatted_address,
            String business, String city, String direction, String distance,
            String district, String province, String street, String street_number,
            int cityCode) {
        this.lng = lng;
        this.lat = lat;
        this.formatted_address = formatted_address;
        this.business = business;
        this.city = city;
        this.direction = direction;
        this.distance = distance;
        this.district = district;
        this.province = province;
        this.street = street;
        this.street_number = street_number;
        this.cityCode = cityCode;
    }

    @Generated(hash = 1723987110)
    public LocationEntity() {
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
