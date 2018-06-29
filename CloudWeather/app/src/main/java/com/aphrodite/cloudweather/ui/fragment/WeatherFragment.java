package com.aphrodite.cloudweather.ui.fragment;

import android.widget.ScrollView;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.ui.base.BaseFragment;
import com.aphrodite.cloudweather.ui.widget.CircleProgressBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import butterknife.BindView;

/**
 * Created by Aphrodite on 2018/6/11.
 */
public class WeatherFragment extends BaseFragment {
    @BindView(R.id.weather_info_list)
    PullToRefreshScrollView mRefreshScrollView;
    @BindView(R.id.circle_progress_bar)
    CircleProgressBar mCircleProgressBar;

    @Override
    protected int getViewId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {
        mRefreshScrollView.setMode(Mode.PULL_FROM_START);
        mCircleProgressBar.startDotAnimator();
        mCircleProgressBar.setMinTemp(20);
        mCircleProgressBar.setMaxTemp(36);
        mRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
