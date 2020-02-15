package com.ju.wyhouse.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author:gaoju
 * Date:2020/1/18 17:08
 * Path:com.ju.wyhouse.view
 * Desc: 自定义提示框
 */
public class DialogView extends Dialog {


    public DialogView(@NonNull Context context, int layout, int style, int gravity) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
    }
}
