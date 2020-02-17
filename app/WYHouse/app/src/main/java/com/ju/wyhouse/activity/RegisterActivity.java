package com.ju.wyhouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.DialogView;
import com.ju.wyhouse.view.LoadingView;

import okhttp3.Request;

public class RegisterActivity extends BaseBackActivity implements View.OnClickListener {

    private EditText register_et_account;
    private EditText register_et_password;
    private EditText register_et_password_wy;
    private EditText register_et_password_wy_two;
    private Button register_btn_register;
    private TextView register_tv_no_jy_account;
    private DialogView mDialogView;
    //dialog
    private LoadingView mLoadingView;
    private TextView single_dialog_title;
    private TextView single_dialog_message;
    private Button single_dialog_yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("注册");
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("注册中...");
        register_et_account = findViewById(R.id.register_et_account);
        register_et_password = findViewById(R.id.register_et_password);
        register_et_password_wy = findViewById(R.id.register_et_password_wy);
        register_et_password_wy_two = findViewById(R.id.register_et_password_wy_two);
        register_btn_register = findViewById(R.id.register_btn_register);
        register_tv_no_jy_account = findViewById(R.id.register_tv_no_jy_account);
        register_tv_no_jy_account.setOnClickListener(this);
        register_btn_register.setOnClickListener(this);
        //初始化dialog
        mDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_single_result, Gravity.BOTTOM);
        single_dialog_title = mDialogView.findViewById(R.id.single_dialog_title);
        single_dialog_message = mDialogView.findViewById(R.id.single_dialog_message);
        single_dialog_yes = mDialogView.findViewById(R.id.single_dialog_yes);
        single_dialog_title.setText("系统提示");
        single_dialog_yes.setText("确定");
        single_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn_register:
                StartRegister();
                break;
            case R.id.register_tv_no_jy_account:
                startActivity(new Intent(this, ForgetJwActivity.class));
                break;
            default:
                break;
            case R.id.single_dialog_yes:
                break;
        }
    }

    //开始进行注册
    private void StartRegister() {

        String account = register_et_account.getText().toString().trim();
        String password = register_et_password.getText().toString().trim();
        String wyPassword = register_et_password_wy.getText().toString().trim();
        String wyPasswordtwo = register_et_password_wy_two.getText().toString().trim();

        //判断两次输入是否一样
        if (!wyPassword.equals(wyPasswordtwo)) {
            ToastUtil.showToast(this, "两次输入的密码不一致");
            return;
        }

        //获取注册地址
        Request request = NetUrl.getRegisterRequest(account, password, wyPassword);
        LogUtil.i("注册request：" + request.url());
        //开始请求
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("注册结果response：" + response);
                mLoadingView.hide();
                jsonParsing(response);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(RegisterActivity.this, msg);
                mLoadingView.hide();
            }


            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(RegisterActivity.this, "网络超时，请稍后再试");
            }
        });
    }


    /**
     * json解析
     *
     * @param response
     */
    private void jsonParsing(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        //成功继续解析
        JSONObject data = jsonObject.getJSONObject("data");
        //获取注册id
        int id = data.getInteger("id");
        single_dialog_message.setText("注册成功！万事屋登录id为" + id + "，请牢记您的登录ID，建议截图保存");
        DialogManager.getInstance().show(mDialogView);
    }


}
