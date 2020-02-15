package com.ju.wyhouse.utils;

import android.text.TextUtils;
import android.util.Log;

import com.ju.wyhouse.BuildConfig;
import com.ju.wyhouse.entity.Constants;

/**
 * Author:gaoju
 * Date:2020/1/18 14:44
 * Path:com.ju.wyhouse.utils
 * Desc: 日志调试类
 */
public class LogUtil {


    public static void i(String text){
        if (BuildConfig.DEBUG){
            if (!TextUtils.isEmpty(text)){
                Log.i(Constants.LOG_NAME,text);
            }
        }
    }


    public static void e(String text){
        if (BuildConfig.DEBUG){
            if (!TextUtils.isEmpty(text)){
                Log.e(Constants.LOG_NAME,text);
            }
        }
    }

}
