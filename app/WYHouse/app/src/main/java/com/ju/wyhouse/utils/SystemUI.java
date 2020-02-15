package com.ju.wyhouse.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

/**
 * Author:gaoju
 * Date:2020/1/18 21:30
 * Path:com.ju.wyhouse.utils
 * Desc: 沉浸式实现
 */
public class SystemUI {

    public static void fixSystemUI(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //获取最顶层的View
            mActivity.getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
