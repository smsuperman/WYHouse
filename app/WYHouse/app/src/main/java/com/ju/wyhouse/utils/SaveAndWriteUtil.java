package com.ju.wyhouse.utils;

import android.text.TextUtils;

import com.ju.wyhouse.entity.Constants;
import com.ju.wyhouse.manager.SpManager;

/**
 * Author:gaoju
 * Date:2020/1/20 17:19
 * Path:com.ju.wyhouse.utils
 * Desc: 保存工具类
 */
public class SaveAndWriteUtil {


    /**
     * 保存Cookie
     *
     * @param cookie
     */
    public static void saveCookie(String cookie) {
        if (!TextUtils.isEmpty(cookie)) {
            SpManager.getInstance().putString(Constants.WSW_COOKIE, cookie);
            LogUtil.i("cookie保存成功：" + cookie);
        } else {
            LogUtil.e("cookie保存失败，为空");
        }
    }

    /**
     * 获取cookie
     *
     * @return
     */
    public static String getCookie() {
        return SpManager.getInstance().getString(Constants.WSW_COOKIE, "");
    }


    /**
     * 保存flag
     *
     * @param wswFlag
     */
    public static void saveFlag(String wswFlag) {
        if (!TextUtils.isEmpty(wswFlag)) {
            SpManager.getInstance().putString(Constants.WSW_FLAG, wswFlag);
            LogUtil.i("flag保存成功：" + wswFlag);
        } else {
            LogUtil.e("flag保存失败，为空");
        }
    }

    /**
     * 获取Flag
     * @return
     */
    public static String getFlagId() {
        return SpManager.getInstance().getString(Constants.WSW_FLAG, "");
    }

    /**
     * 清空cookie和flagId
     */
    public static void removeFlagIdAndCookie() {
        SpManager.getInstance().putString(Constants.WSW_COOKIE, "");
        SpManager.getInstance().putString(Constants.WSW_FLAG, "");
        LogUtil.i("flagId和cookie清空完毕");
    }
}
