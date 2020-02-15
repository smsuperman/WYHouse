package com.ju.wyhouse.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ju.wyhouse.R;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.utils.AnimUtils;

/**
 * Author:gaoju
 * Date:2020/1/19 12:00
 * Path:com.ju.wyhouse.view
 * Desc: 加载提示框
 */
public class LoadingView {

    private DialogView mLoadingView;
    private ImageView iv_loading;
    private TextView tv_loading_text;
    private ObjectAnimator mAnim;


    public LoadingView(Context context) {
        mLoadingView = DialogManager.getInstance().initView(context, R.layout.dialog_loding);
        iv_loading = mLoadingView.findViewById(R.id.iv_loding);
        tv_loading_text = mLoadingView.findViewById(R.id.tv_loding_text);
        mAnim = AnimUtils.ratation(iv_loading);
        //外面不能点击
        mLoadingView.setCancelable(false);
    }


    /**
     * 设置加载框文本
     *
     * @param text
     */
    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_loading_text.setText(text);
        }
    }

    public void show() {
        mAnim.start();
        DialogManager.getInstance().show(mLoadingView);
    }


    public void show(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_loading_text.setText(text);
        }
        mAnim.start();
        DialogManager.getInstance().show(mLoadingView);
    }


    public void hide() {
        mAnim.pause();
        DialogManager.getInstance().hide(mLoadingView);
    }
}
