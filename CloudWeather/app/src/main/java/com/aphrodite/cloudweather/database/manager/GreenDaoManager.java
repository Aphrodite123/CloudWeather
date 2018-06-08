package com.aphrodite.cloudweather.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aphrodite.cloudweather.config.BaseConfig;
import com.aphrodite.cloudweather.database.upgrade.DBMigrationHelper;
import com.usher.greendao.greendao.gen.DaoMaster;
import com.usher.greendao.greendao.gen.DaoSession;

/**
 * Created by Aphrodite on 2017/5/16.
 */

public class GreenDaoManager {
    private static GreenDaoManager sInstance = null;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private GreenDaoManager(Context context) {
        /*通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper注意：默认的 DaoMaster.DevOpenHelper会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。*/
        DBMigrationHelper migrationHelper = new DBMigrationHelper(context, BaseConfig.NAME_DATABASE, null);
        //注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        SQLiteDatabase sqLiteDatabase = migrationHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(sqLiteDatabase);
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (sInstance == null) {
                    sInstance = new GreenDaoManager(context);
                }
            }
        }
        return sInstance;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeDaoSession();
    }

    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

}
