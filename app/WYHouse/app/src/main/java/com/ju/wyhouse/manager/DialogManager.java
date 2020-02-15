package com.ju.wyhouse.manager;

import android.content.Context;
import android.view.Gravity;

import com.ju.wyhouse.R;
import com.ju.wyhouse.view.DialogView;

/**
 * Author:gaoju
 * Date:2020/1/18 17:07
 * Path:com.ju.wyhouse.manager
 * Desc: 提示框管理类
 */
public class DialogManager {

    private volatile static DialogManager mInstance;

    private DialogManager() {

    }


    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    public DialogView initView(Context context, int layout) {
        return new DialogView(context, layout, R.style.Theme_Dialog, Gravity.CENTER);
    }

    public DialogView initView(Context context, int layout, int gravity) {
        return new DialogView(context, layout, R.style.Theme_Dialog, gravity);
    }

    public void show(DialogView view) {
        if (view != null) {
            if (!view.isShowing()) {
                view.show();
            }
        }
    }


    public void hide(DialogView view) {
        if (view != null) {
            if (view.isShowing()) {
                view.dismiss();
            }
        }
    }

}
