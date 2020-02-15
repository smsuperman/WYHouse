package com.ju.wyhouse.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Author:gaoju
 * Date:2020/1/18 14:21
 * Path:com.ju.wyhouse.utils
 * Desc:
 */
public class ToastUtil {


    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
