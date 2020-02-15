package com.ju.wyhouse.application;

import android.app.Application;

import com.ju.wyhouse.service.PushIntentService;
import com.ju.wyhouse.service.PushService;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.manager.SpManager;
import com.tencent.bugly.Bugly;

import org.litepal.LitePal;

/**
 * Author:gaoju
 * Date:2020/1/18 16:52
 * Path:com.ju.wyhouse.application
 * Desc:
 */
public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initFrameWork();
    }

    /**
     * 初始化框架
     */
    private void initFrameWork() {
        SpManager.getInstance().initSp(this);
        // 初始化LitePal数据库
        LitePal.initialize(this);
        LogUtil.i("Framework框架初始化");
    }


}
