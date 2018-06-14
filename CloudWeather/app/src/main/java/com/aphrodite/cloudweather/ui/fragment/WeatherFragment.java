package com.aphrodite.cloudweather.ui.fragment;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.ui.base.BaseFragment;
import com.aphrodite.cloudweather.ui.widget.NowHwWeatherView;

import butterknife.BindView;

/**
 * Created by Aphrodite on 2018/6/11.
 */
public class WeatherFragment extends BaseFragment {
    @BindView(R.id.circle_progress_bar)
    NowHwWeatherView mCircleProgressBar;

    @Override
    protected int getViewId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {
        mCircleProgressBar.startDotAnimator();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
