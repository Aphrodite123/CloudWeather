package com.aphrodite.cloudweather.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Aphrodite on 2018/6/8. 汉语转拼音
 */
public class PinyinUtils {
    private static final String TAG = PinyinUtils.class.getSimpleName();

    /**
     * 获取汉字字符串的首字母，英文字符不变
     *
     * @param chinese
     * @return
     */
    public static String getFirstLetterForChar(String chinese) {
        if (TextUtils.isEmpty(chinese)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        char[] chars = chinese.toCharArray();
        if (ObjectUtils.isEmpty(chars)) {
            return null;
        }

        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > 128) {
                try {
                    buffer.append(PinyinHelper.toHanyuPinyinStringArray(chars[i], outputFormat)[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    Logger.w(TAG, "Enter getFirstLetter method.BadHanyuPinyinOutputFormatCombination: " + e);
                }
            } else {
                buffer.append(chars[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 获取汉字字符串的第一个字母
     *
     * @param chinese
     * @return
     */
    public static String getFirstLetter(String chinese) {
        if (TextUtils.isEmpty(chinese)) {
            return null;
        }

        char c = chinese.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        StringBuffer buffer = new StringBuffer();
        if (ObjectUtils.isEmpty(pinyinArray)) {
            buffer.append(c);
        } else {
            buffer.append(pinyinArray[0].charAt(0));
        }
        return buffer.toString();
    }

    /**
     * 获取汉字字符串的汉语拼音，英文字符不变
     *
     * @param chinese
     * @return
     */
    public static String getPinyin(String chinese) {
        if (TextUtils.isEmpty(chinese)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        char[] chars = chinese.toCharArray();
        if (ObjectUtils.isEmpty(chars)) {
            return null;
        }

        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > 128) {
                try {
                    buffer.append(PinyinHelper.toHanyuPinyinStringArray(chars[i], outputFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    Logger.w(TAG, "Enter getPinyin method.BadHanyuPinyinOutputFormatCombination: " + e);
                }
            } else {
                buffer.append(chars[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 拼音字符串首字母转换成大写
     *
     * @param pinyin
     * @return
     */
    public static String convertFirstCharToUppercase(String pinyin) {
        if (TextUtils.isEmpty(pinyin)) {
            return null;
        }

        if (Character.isUpperCase(pinyin.charAt(0))) {
            return pinyin;
        } else {
            StringBuffer buffer = new StringBuffer();
            buffer.append(Character.toUpperCase(pinyin.charAt(0))).append(pinyin.substring(1));
            return buffer.toString();
        }
    }

}
