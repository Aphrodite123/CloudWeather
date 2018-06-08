package com.aphrodite.cloudweather.ui.base;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.aphrodite.cloudweather.ui.manager.ActivitysManager;
import com.aphrodite.cloudweather.ui.widget.ManagerLoadingDialog;
import com.aphrodite.cloudweather.utils.ManagerLoadingUtil;
import com.aphrodite.cloudweather.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * Created by Aphrodite on 2017/9/11.
 */

public abstract class BaseActivity extends FragmentActivity {

    private static final int ENABLE_CANCEL_LOADING = 1;
    private static final int DELAY_ENABLE_CANCEL_LOADING = 15 * 1000;
    protected ManagerLoadingDialog mLoadingDialog;
    protected ActivitysManager mActivityManager;
    protected Handler mHandler;

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

    protected int getWidth() {
        return UIUtils.getDisplayWidthPixels(this);
    }

    protected int getHeight() {
        return UIUtils.getDisplayHeightPixels(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHandler = new Handler() {
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
        mActivityManager = ActivitysManager.getInstance();
        mActivityManager.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initView();
        initData();
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

}
