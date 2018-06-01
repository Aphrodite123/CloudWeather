package com.aphrodite.cloudweather.utils;

import com.aphrodite.cloudweather.config.BaseConfig;

import java.io.UnsupportedEncodingException;

/**
 * Created by Aphrodite on 2017/11/19.
 */

public class TextUtils {
    private static final String TAG = TextUtils.class.getSimpleName();

    public static byte[] getBytesByUTF8(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (BaseConfig.DEBUG) {
                Logger.d(TAG, "Enter getBytesByUTF8 method.UnsupportedEncodingException: " + e);
            }
            return null;
        }
    }
}
