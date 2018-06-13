package com.aphrodite.cloudweather.ui.base;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.aphrodite.cloudweather.ui.manager.ActivitysManager;
import com.aphrodite.cloudweather.ui.widget.ManagerLoadingDialog;
import com.aphrodite.cloudweather.utils.ManagerLoadingUtil;
import com.aphrodite.cloudweather.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Aphrodite on 2017/9/11.
 */

public abstract class BaseActivity extends FragmentActivity {

    private static final int ENABLE_CANCEL_LOADING = 1;
    private static final int DELAY_ENABLE_CANCEL_LOADING = 15 * 1000;
    protected ManagerLoadingDialog mLoadingDialog;
    protected ActivitysManager mActivityManager;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENABLE_CANCEL_LOADING: {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.setCancelable(true);
                    }
                    break;
                }

                default:
                    break;
            }
        }
    };

    private DialogInterface.OnDismissListener mLoadingDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            loadingDialogDismissed();
        }
    };

    private void loadingDialogDismissed() {
        mHandler.removeMessages(ENABLE_CANCEL_LOADING);
        mLoadingDialog = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        settingToolbar();
        settingVirtualKey();
        initConfig();
        initView();
        initData();
    }

    private void settingToolbar() {
        // 4.4以上设备适用，4.4以下使用系统默认颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置华为手机虚拟键
     */
    private void settingVirtualKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0 以上 全透明
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
            window.setStatusBarColor(Color.TRANSPARENT);
            // 虚拟导航键
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 以上 半透明
            Window window = getWindow();
            // 状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 虚拟导航键
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void initConfig() {
        /**
         * 绑定Activity(注:必须在setContentView之后)
         */
        ButterKnife.bind(this);
    }

    /**
     * 获取layout
     *
     * @return
     */
    protected abstract int getViewId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
        mActivityManager = ActivitysManager.getInstance();
        mActivityManager.addActivity(this);

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        mActivityManager.finishLastActivity();
        dismissLoadingDialog();
        super.onDestroy();
    }

    @Override
    public void finish() {
        hideKeyBoard();
        super.finish();
    }

    public boolean isFrontActivity() {
        return mActivityManager.isFrontActivity(this);
    }

    public void showKeyBoard(EditText editText) {
        editText.clearFocus();
        editText.requestFocus();
        UIUtils.openSoftKeyboard(editText);
    }

    public void hideKeyBoard() {
        UIUtils.closeSoftKeyboard(this);
    }

    public void showLoadingDialog() {
        showLoadingDialog(false);
    }

    public void showLoadingDialog(boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = ManagerLoadingUtil.show(this, cancelable);
            if (!cancelable) {
                mHandler.sendEmptyMessageDelayed(ENABLE_CANCEL_LOADING, DELAY_ENABLE_CANCEL_LOADING);
            }
            mLoadingDialog.setOnDismissListener(mLoadingDismissListener);
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 动态检测权限
     *
     * @return
     */
    @TargetApi(23)
    protected abstract boolean hasPermission();

    /**
     * 权限申请
     */
    @TargetApi(23)
    protected abstract void requestPermission();

    protected int getWidth() {
        return UIUtils.getDisplayWidthPixels(this);
    }

    protected int getHeight() {
        return UIUtils.getDisplayHeightPixels(this);
    }

}
