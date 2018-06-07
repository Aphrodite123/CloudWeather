package com.aphrodite.cloudweather.http.bean;

import java.io.Serializable;

/**
 * Created by Aphrodite on 2018/6/7.
 */
public class LocationBean implements Serializable {
    private static final long serialVersionUID = 183947305628777579L;

    private String formatted_address;

    private String business;

    private int cityCode;

    private Location location;

    private AddressComponent addressComponent;

    public class Location {
        private double lng;
        private double lat;

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
    }

    public class AddressComponent {
        private String city;
        private String direction;
        private String distance;
        private String district;
        private String province;
        private String street;
        private String street_number;

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

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AddressComponent getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }
}
