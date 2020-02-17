package com.ju.wyhouse.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.model.FindIdModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.DialogView;

import okhttp3.Request;

public class FindIdActivity extends BaseBackActivity implements View.OnClickListener {

    private EditText find_id_et_account;
    private Button find_id_btn_find;
    private DialogView mDialogView;
    private TextView single_dialog_title;
    private TextView single_dialog_message;
    private Button single_dialog_yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        initView();
        initDiaLog();
    }

    private void initDiaLog() {
        mDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_single_result, Gravity.BOTTOM);
        single_dialog_title = mDialogView.findViewById(R.id.single_dialog_title);
        single_dialog_message = mDialogView.findViewById(R.id.single_dialog_message);
        single_dialog_yes = mDialogView.findViewById(R.id.single_dialog_yes);
        single_dialog_title.setText("系统提示");
        single_dialog_yes.setText("确定");
        single_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mDialogView);
            }
        });
    }

    private void initView() {
        getSupportActionBar().setTitle("找回万事屋ID");
        find_id_et_account = findViewById(R.id.find_id_et_account);
        find_id_btn_find = findViewById(R.id.find_id_btn_find);
        find_id_btn_find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_id_btn_find:
                startFindId();
                break;
            default:
                break;
        }
    }

    private void startFindId() {
        String account = find_id_et_account.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            ToastUtil.showToast(this, "输入框不能为空！");
            return;
        }
        Request request = NetUrl.getFindIdRequest(account);
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                parsingRes(response);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(FindIdActivity.this, msg);
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(FindIdActivity.this, "网络超时，请稍后再试");
            }
        });

    }

    /**
     * 解析结果
     *
     * @param response
     */
    private void parsingRes(String response) {
        FindIdModel findIdModel = JSON.parseObject(response, new TypeReference<FindIdModel>() {
        });
        if (findIdModel.getData() == null) {
            single_dialog_message.setText("该账号未在武院万事屋进行注册，请在注册界面注册");
            DialogManager.getInstance().show(mDialogView);
        } else {
            single_dialog_message.setText("该账号的万事屋ID为：" + findIdModel.getData().getId());
            DialogManager.getInstance().show(mDialogView);
        }
    }
}

