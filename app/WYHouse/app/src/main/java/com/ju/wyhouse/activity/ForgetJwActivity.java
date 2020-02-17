package com.ju.wyhouse.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.TorchState;

import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;

import okhttp3.Request;

public class ForgetJwActivity extends BaseBackActivity implements View.OnClickListener {

    private EditText jw_et_account;
    private EditText jw_et_body_number;
    private Button jw_btn_remake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_jw);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("找回教务处密码");
        jw_et_account = findViewById(R.id.jw_et_account);
        jw_et_body_number = findViewById(R.id.jw_et_body_number);
        jw_btn_remake = findViewById(R.id.jw_btn_remake);
        jw_btn_remake.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jw_btn_remake:
                startRemakeAccount();
                break;
            default:
                break;
        }
    }

    private void startRemakeAccount() {
        String account = jw_et_account.getText().toString().trim();
        String bodyNumber = jw_et_body_number.getText().toString().trim();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(bodyNumber)) {
            ToastUtil.showToast(this, "输入框不能为空");
        }

        Request request = NetUrl.getForgetJwAccountRequest(account, bodyNumber);
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                ToastUtil.showToast(ForgetJwActivity.this, "重置成功");
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(ForgetJwActivity.this, msg);
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(ForgetJwActivity.this, "网络超时，请稍后再试");
            }
        });
    }


}
