package com.aphrodite.cloudweather.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aphrodite.cloudweather.ui.fragment.WeatherFragment;

import java.util.List;

/**
 * Created by Aphrodite on 2018/6/11.
 */
public class WeatherFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<WeatherFragment> mFragmentList;

    public WeatherFragmentAdapter(FragmentManager manager, Context context, List<WeatherFragment> fragmentList) {
        super(manager);
        this.mContext = context;
        this.mFragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

}
