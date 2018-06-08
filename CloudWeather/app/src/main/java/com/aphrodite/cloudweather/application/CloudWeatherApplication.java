package com.aphrodite.cloudweather.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.aphrodite.cloudweather.config.BaseConfig;
import com.aphrodite.cloudweather.database.manager.GreenDaoManager;
import com.aphrodite.cloudweather.database.upgrade.RealmMigrationHelper;
import com.aphrodite.cloudweather.utils.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aphrodite on 2017/9/16.
 */

public class CloudWeatherApplication extends Application {
    private static final String TAG = CloudWeatherApplication.class.getSimpleName();

    private static CloudWeatherApplication mApplication = null;
    private static GreenDaoManager mDaoManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initSystem();
    }

    private void initSystem() {
        this.mApplication = this;

        Logger.init(this);

        initGreenDao();
    }

    private void initGreenDao() {
        this.mDaoManager = GreenDaoManager.getInstance(this);
    }

    public static CloudWeatherApplication getApp() {
        return mApplication;
    }

    public static GreenDaoManager getDaoManager() {
        return mDaoManager;
    }

    public static void setDaoManager(GreenDaoManager daoManager) {
        CloudWeatherApplication.mDaoManager = daoManager;
    }

    /**
     * 获取当前Activity名称
     *
     * @return
     */
    public String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    public void exitApp() {
        mDaoManager.closeDataBase();
    }

}
