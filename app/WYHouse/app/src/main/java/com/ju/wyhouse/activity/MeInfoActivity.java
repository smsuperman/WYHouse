package com.ju.wyhouse.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.db.DbUserData;
import com.ju.wyhouse.model.UpdateUserModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;

import okhttp3.Request;

public class MeInfoActivity extends BaseBackActivity implements View.OnClickListener {

    private EditText me_info_nickname;
    private TextView me_info_name;
    private TextView me_info_school_number;
    private TextView me_info_school;
    private Button me_info_btn_save;
    private TextView me_info_id;
    private LoadingView mLoadingView;
    public final static int UPDATE_ME_INFO_FLAG = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);
        initView();
        initMeInfoData();
    }

    /**
     * 初始化个人信息
     */
    private void initMeInfoData() {
        DbUserData dbUserData = DbManager.getInstance().queryMeUser();
        if (dbUserData != null) {
            me_info_id.setText(String.valueOf(dbUserData.getUserId()));
            me_info_nickname.setText(dbUserData.getName());
            me_info_name.setText(dbUserData.getJwName());
            me_info_school_number.setText(dbUserData.getJwUser());
            me_info_school.setText(dbUserData.getJwBanji());
        }
    }

    private void initView() {
        getSupportActionBar().setTitle("个人信息");
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("更新中...");
        me_info_id = findViewById(R.id.me_info_id);
        me_info_nickname = findViewById(R.id.me_info_nickname);
        me_info_name = findViewById(R.id.me_info_name);
        me_info_school_number = findViewById(R.id.me_info_school_number);
        me_info_school = findViewById(R.id.me_info_school);
        me_info_btn_save = findViewById(R.id.me_info_btn_save);
        me_info_btn_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_info_btn_save:
                updateMeInfo();
                break;
            default:
                break;
        }
    }


    /**
     * 更新个人信息
     */
    private void updateMeInfo() {
        if (TextUtils.isEmpty(me_info_nickname.getText().toString().trim())) {
            ToastUtil.showToast(this, "昵称不能为空！");
            return;
        }

        Request request = NetUrl.getUpDateMeInfoRequest("", me_info_nickname.getText().toString().trim());
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                UpdateUserModel updateUserModel = JSON.parseObject(response, new TypeReference<UpdateUserModel>() {
                });
                if (updateUserModel.getCode() == 0) {
                    ToastUtil.showToast(MeInfoActivity.this, "更新个人信息成功");
                    //保存昵称数据库
                    DbManager.getInstance().updateMeNickName(updateUserModel.getData().getName());
                } else {
                    ToastUtil.showToast(MeInfoActivity.this, "更新个人信息失败，请稍后再试");
                }
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("更新失败：" + msg);
                ToastUtil.showToast(MeInfoActivity.this, "更新头像失败，请稍后再试");
                mLoadingView.hide();
            }
        });


    }


}
