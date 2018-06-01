package com.aphrodite.cloudweather.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Aphrodite on 2017/9/16.
 */

public class BaseConfig {
    public static final boolean DEBUG = true;

    public static final String PACKAGE_NAME = "com.aphrodite.cloudweather";

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    public static final String ROOT_PATH = SDCARD_PATH + "manager" + File.separator;

    /**
     * 数据库名称
     */
    public static final String NAME_DATABASE = "manager.db";

    /**
     * Realm名称
     */
    public static final String NAME_REALM = "manager.realm";

    /**
     * 数据库版本号
     */
    public static final long DB_VERSION = 1;

    /**
     * Log configuration
     */
    public static final String LOG_FILE_PATH = ROOT_PATH + "logs" + File.separator;
    public static final String LOG_FILE_SUFFIX = ".log";
}
