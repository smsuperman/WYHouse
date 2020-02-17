package com.ju.wyhouse.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.db.DbUserData;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.HttpRequest;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;

import okhttp3.Request;

public class UpdateJwActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView update_info_id;
    private TextView update_jw_account;
    private EditText update_info_jwpassword;
    private Button update_btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jw);
        initView();
        initMeInfo();
    }

    private void initMeInfo() {

        DbUserData dbUserData = DbManager.getInstance().queryMeUser();
        if (dbUserData != null) {
            update_info_id.setText(String.valueOf(dbUserData.getUserId()));
            update_jw_account.setText(String.valueOf(dbUserData.getJwUser()));
        }
    }

    private void initView() {
        getSupportActionBar().setTitle("更新绑定");
        update_info_id = findViewById(R.id.update_info_id);
        update_jw_account = findViewById(R.id.update_jw_account);
        update_info_jwpassword = findViewById(R.id.update_info_jwpassword);
        update_btn_update = findViewById(R.id.update_btn_update);
        update_btn_update.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn_update:
                startUpdateAccount();
                break;
            default:
                break;
        }
    }

    private void startUpdateAccount() {
        if (TextUtils.isEmpty(update_info_jwpassword.getText().toString())) {
            ToastUtil.showToast(this, "输入框不能为空");
            return;
        }

        Request request =
                NetUrl.getUpdateAccountRequest(update_jw_account.getText().toString().trim(), update_info_jwpassword.getText().toString());
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                ToastUtil.showToast(UpdateJwActivity.this, "更新成功");
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(UpdateJwActivity.this, msg);
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(UpdateJwActivity.this, "网络超时，请稍后再试");
            }
        });
    }
}
