package com.ju.wyhouse.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ju.wyhouse.MainActivity;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseUiActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.db.DbUserData;
import com.ju.wyhouse.entity.Constants;
import com.ju.wyhouse.model.UserModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;

import okhttp3.Request;


/**
 * 欢迎界面
 */
public class WelcomeActivity extends BaseUiActivity {

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Constants.SKIP_FLAG:
                    startMain();
                    break;
                default:
                    break;
            }
            return false;
        }

    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler.sendEmptyMessageDelayed(Constants.SKIP_FLAG, 1000);
    }


    private void startMain() {
        //获取本地用户缓存，如果有就进行登录获取用户最新数据
        DbUserData dbUserData = DbManager.getInstance().queryMeUser();
        if (dbUserData != null) {
            requestLogin();
        } else {
            //没有跳转就到登录界面
            startLoginActivity();
            finish();
        }
    }


    /**
     * 进行登录请求
     */
    private void requestLogin() {
        Request request = NetUrl.getFlagIdLoginRequest();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                parsingLoginResultJson(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("网络超时，请稍后再试：" + msg);
                ToastUtil.showToast(WelcomeActivity.this, "网络超时，请稍后再试");
            }
        });


    }

    /**
     * 解析请求结果
     *
     * @param response
     */
    private void parsingLoginResultJson(String response) {
        UserModel userModel = JSON.parseObject(response, new TypeReference<UserModel>() {
        });
        LogUtil.i("登录用户response：" + response);
        if (userModel.getCode() == 0 && userModel.getData() != null) {
            //更新数据库
            DbManager.getInstance().updateMeUser(userModel);
            SaveAndWriteUtil.saveCookie(userModel.getCookie());
            SaveAndWriteUtil.saveFlag(userModel.getData().getWswFlag());
            startMainActivity();
        } else {
            ToastUtil.showToast(WelcomeActivity.this, "登录失效，请重新登录");
            //删除数据库
            DbManager.getInstance().removeMeUser();
            SaveAndWriteUtil.removeFlagIdAndCookie();
            startLoginActivity();
        }
    }


    /**
     * 跳转到登录界面
     */
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * 跳转到主界面
     */
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
