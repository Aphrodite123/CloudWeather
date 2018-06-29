package com.aphrodite.cloudweather.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aphrodite.cloudweather.utils.Logger;
import com.aphrodite.cloudweather.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Aphrodite on 2016/10/7.
 */

public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private boolean onShow = false;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(getViewId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
    }

    /**
     * 获取layout
     *
     * @return
     */
    protected abstract int getViewId();

    /**
     * 初始化layout
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化控件监听方法，非外部调用方法，子类实现子控件的监听函数
     */
    protected abstract void initListener();

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && !onShow) {
            onShow = true;
        }

        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (onShow) {
            onShow = false;
        }

        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.d(TAG, "Enter setUserVisibleHint method, isVisibleToUser: " + isVisibleToUser + " onShow: " + onShow);
        if (isVisibleToUser && !onShow) {
            onShow = true;
        } else if (!isVisibleToUser && onShow) {
            onShow = false;
        }
    }

    protected void showKeyBoard(EditText editText) {
        editText.clearFocus();
        editText.requestFocus();
        UIUtils.openSoftKeyboard(editText);
    }

    protected void hideKeyBoard() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        UIUtils.closeSoftKeyboard(activity);
    }
}
