package com.aphrodite.cloudweather.view.inter;

import android.location.Address;

import java.util.List;

/**
 * Created by Aphrodite on 2018/6/4.
 */
public interface ILocation {
    void onLocationFailed();

    void onLocationSuccess(List<Address> addresses);
}
