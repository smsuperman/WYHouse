package com.ju.wyhouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ju.wyhouse.MainActivity;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.model.UserModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;

import okhttp3.Request;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText login_et_account;
    private EditText login_et_password;
    private Button login_btn_login;
    private TextView login_tv_no_account;
    private TextView login_find_id;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        login_et_account = findViewById(R.id.login_et_account);
        login_et_password = findViewById(R.id.login_et_password);
        login_btn_login = findViewById(R.id.login_btn_login);
        login_tv_no_account = findViewById(R.id.login_tv_no_account);
        login_find_id = findViewById(R.id.login_find_id);
        login_btn_login.setOnClickListener(this);
        login_tv_no_account.setOnClickListener(this);
        login_find_id.setOnClickListener(this);

        mLoadingView = new LoadingView(this);
        mLoadingView.setText("正在登陆中...");
    }


    /**
     * 进行请求登录
     */
    private void login() {

        String account = login_et_account.getText().toString().trim();
        String password = login_et_password.getText().toString().trim();

        //获取登录连接
        Request request = NetUrl.getLoginRequest(account, password);
        LogUtil.i("登录request：" + request.url());
        mLoadingView.show();
        //进行登录
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("登录结果response：" + response);
                checkLogin(response);
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(LoginActivity.this, msg);
                mLoadingView.hide();
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(LoginActivity.this, "网络超时，请稍后再试");
            }
        });
    }


    /**
     * 检查是否登录成功 -6 账号和密码错误 -2 系统错误
     *
     * @param response
     */
    private void checkLogin(String response) {
        UserModel userModel = JSON.parseObject(response, new TypeReference<UserModel>() {
        });


        if (userModel.getCode() == 0) {
            //保存
            SaveAndWriteUtil.saveFlag(userModel.getData().getWswFlag());
            SaveAndWriteUtil.saveCookie(userModel.getCookie());
            //保存到数据库
            DbManager.getInstance().saveMeUser(userModel);
            //进入主界面
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (userModel.getCode() == -6) {
            ToastUtil.showToast(LoginActivity.this, "账号或者密码错误");
        } else if (userModel.getCode() == -2) {
            ToastUtil.showToast(LoginActivity.this, "系统错误，请稍后再试");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                //开始登陆
                login();
                break;
            case R.id.login_tv_no_account:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.login_find_id:
                startActivity(new Intent(LoginActivity.this, FindIdActivity.class));
                break;
            default:
                break;
        }
    }


}
