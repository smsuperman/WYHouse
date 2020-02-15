package com.ju.wyhouse.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Author:gaoju
 * Date:2020/1/6 21:48
 * Path:com.example.framework.utils
 * Desc:动画工具类
 */
public class AnimUtils {


    //选择动画工具类
    public static ObjectAnimator ratation(View view) {
        //旋转音乐图标
        ObjectAnimator mAnim = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        mAnim.setDuration(6000);
        //循环播放
        mAnim.setRepeatMode(ValueAnimator.RESTART);
        //一直播放
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        //插值器
        mAnim.setInterpolator(new LinearInterpolator());
        return mAnim;
    }
}
