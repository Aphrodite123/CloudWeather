package com.aphrodite.cloudweather.config;

/**
 * Created by Aphrodite on 2017/9/16.
 */

public interface IntentAction {
    public static final String ACTION_SUFFIX = BaseConfig.PACKAGE_NAME + ".";

    /**
     * SecondActivity
     */
    public interface SecondActivityAction {
        String ACTION = ACTION_SUFFIX + "ui.activity.SECOND";
    }

    /**
     * ThirdActivity
     */
    public interface ThirdActivityAction {
        String ACTION = ACTION_SUFFIX + "ui.activity.THIRD";
    }
}
