package com.ju.wyhouse.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:gaoju
 * Date:2020/1/26 14:29
 * Path:com.ju.wyhouse.utils
 * Desc:时间相关
 */
public class DateUtil {

    private static SimpleDateFormat mSimpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd");
    //系统当前时间
    private static Calendar selectedDate;
    //精确时间
    private static SimpleDateFormat mSimpleDataTimeFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");


    /**
     * 获取当前日志 格式xxxx-xx-xx
     *
     * @return
     */
    public static String getDateNow() {
        return mSimpleDateFormat.format(new Date());
    }


    public static String getDateTimeNow(){
        return mSimpleDataTimeFormat.format(new Date());
    }

    /**
     * 获取系统当前时间，TimePickerView的setDate
     */
    public static Calendar getPickerViewDate() {
        return selectedDate = Calendar.getInstance();
    }

    /**
     * 获取PickerView选择的时间
     */
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        LogUtil.i( "choice date millis: " + date.getTime());
        return mSimpleDateFormat.format(date);
    }

}
