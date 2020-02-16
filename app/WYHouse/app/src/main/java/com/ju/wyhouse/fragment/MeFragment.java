package com.ju.wyhouse.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ju.wyhouse.R;
import com.ju.wyhouse.activity.AppInfoActivity;
import com.ju.wyhouse.activity.LoginActivity;
import com.ju.wyhouse.activity.MeInfoActivity;
import com.ju.wyhouse.activity.MessageActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.db.DbUserData;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.manager.SystemHelperManager;
import com.ju.wyhouse.model.UpdateUserModel;
import com.ju.wyhouse.model.UserModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.GlideUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.DialogView;
import com.ju.wyhouse.view.LoadingView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.bugly.beta.Beta;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * 我的
 */
public class MeFragment extends Fragment implements View.OnClickListener {


    private CircleImageView me_frag_circle;
    private TextView me_frag_nickname;
    private TextView me_frag_classroom;
    private LinearLayout ll_me_user;
    private LinearLayout ll_me_message;
    private LinearLayout ll_me_setting;
    private LinearLayout ll_app_info;
    private LinearLayout ll_me_update;
    private LinearLayout ll_me_exit;
    private DialogView mDialogView;
    private TextView double_dialog_title;
    private TextView double_dialog_message;
    private Button double_dialog_no;
    private Button double_dialog_yes;
    private DialogView mPhotoSelectView;
    private String cirPath = "";
    private TextView tv_ablum;
    private TextView tv_cancel;
    private LoadingView mLoadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        initPhotoView();
        initMeInfo();
        return view;
    }


    /**
     * 初始化选择框
     */
    private void initPhotoView() {
        mPhotoSelectView = DialogManager.getInstance().initView(getActivity(), R.layout.dialog_select_photo, Gravity.BOTTOM);
        tv_ablum = mPhotoSelectView.findViewById(R.id.tv_ablum);
        tv_ablum.setOnClickListener(this);
        tv_cancel = mPhotoSelectView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
    }


    private void initView(View view) {
        //初始化DialogView
        mDialogView = DialogManager.getInstance().initView(getActivity(), R.layout.dialog_double_result, Gravity.BOTTOM);
        double_dialog_title = mDialogView.findViewById(R.id.double_dialog_title);
        double_dialog_message = mDialogView.findViewById(R.id.double_dialog_message);
        double_dialog_no = mDialogView.findViewById(R.id.double_dialog_no);
        double_dialog_yes = mDialogView.findViewById(R.id.double_dialog_yes);
        double_dialog_no.setText("取消");
        double_dialog_yes.setText("确定");
        double_dialog_title.setText("系统提示");
        double_dialog_message.setText("您确定要退出登录吗");
        double_dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogView.hide();
            }
        });
        double_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空Cookie和flagId，并删除数据库账户
                SaveAndWriteUtil.removeFlagIdAndCookie();
                DbManager.getInstance().removeMeUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        mLoadingView = new LoadingView(getActivity());
        mLoadingView.setText("请稍等...");

        me_frag_circle = view.findViewById(R.id.me_frag_circle);
        me_frag_nickname = view.findViewById(R.id.me_frag_nickname);
        me_frag_classroom = view.findViewById(R.id.me_frag_classroom);
        ll_me_user = view.findViewById(R.id.ll_me_user);
        ll_me_message = view.findViewById(R.id.ll_me_message);
        ll_me_setting = view.findViewById(R.id.ll_me_setting);
        ll_app_info = view.findViewById(R.id.ll_app_info);
        ll_me_exit = view.findViewById(R.id.ll_me_exit);
        ll_me_update = view.findViewById(R.id.ll_me_update);
        me_frag_circle.setOnClickListener(this);
        ll_me_user.setOnClickListener(this);
        ll_me_message.setOnClickListener(this);
        ll_me_setting.setOnClickListener(this);
        ll_app_info.setOnClickListener(this);
        ll_me_exit.setOnClickListener(this);
        ll_me_update.setOnClickListener(this);
    }

    /**
     * 初始化个人信息
     */
    private void initMeInfo() {
        DbUserData dbUserData = DbManager.getInstance().queryMeUser();
        if (dbUserData != null) {
            me_frag_nickname.setText(dbUserData.getName());
            me_frag_classroom.setText(dbUserData.getJwBanji());
            LogUtil.i("自己id：" + dbUserData.getUserId());
            GlideUtil.loadUrl(getActivity(), dbUserData.getImage(), me_frag_circle);
        }
    }


    /**
     * 获取最新的个人信息
     */
    private void updateMeData() {
        Request request = NetUrl.getFlagIdLoginRequest();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取用户信息response：" + response);
                parsingMeInfoJson(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("获取用户信息网络请求错误");
                ToastUtil.showToast(getActivity(), "获取个人信息超时，请稍后再试");
                mLoadingView.hide();
            }
        });

    }


    /**
     * 进行解析更新的个人信息
     *
     * @param response
     */
    private void parsingMeInfoJson(String response) {
        mLoadingView.hide();
        UserModel userModel = JSON.parseObject(response, new TypeReference<UserModel>() {
        });
        if (userModel.getCode() != 0 || userModel.getData() == null) {
            ToastUtil.showToast(getActivity(), "获取用户信息失败，请稍后再试或者联系管理员");
            return;
        }
        //保存flag和Cookie
        SaveAndWriteUtil.saveFlag(userModel.getData().getWswFlag());
        SaveAndWriteUtil.saveCookie(userModel.getCookie());
        //更数据库
        DbManager.getInstance().updateMeUser(userModel);
        //更新数据
        initMeInfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_me_user:
                //个人信息
                startActivityForResult(new Intent(getContext(), MeInfoActivity.class), MeInfoActivity.UPDATE_ME_INFO_FLAG);
                break;
            case R.id.ll_me_message:
                //开启通知
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.ll_me_setting:
                //设置，先隐藏掉了
                break;
            case R.id.ll_me_exit:
                //退出登录
                mDialogView.show();
                break;
            case R.id.ll_app_info:
                startActivity(new Intent(getActivity(), AppInfoActivity.class));
                break;
            case R.id.ll_me_update:
                //检查更新
                Beta.checkUpgrade();
                break;
            case R.id.me_frag_circle:
                //头像
                DialogManager.getInstance().show(mPhotoSelectView);
                break;
            case R.id.tv_ablum:
                DialogManager.getInstance().hide(mPhotoSelectView);
                //更换头像
                SystemHelperManager.getInstance().openPictureSelectorCir(this);
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoSelectView);
                break;
            default:
                break;
        }
    }


    /**
     * 上传头像文件
     */
    private void upLoadCirImage() {
        Request request = NetUrl.getUpLoadImageFile(new File(cirPath));
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("上传头像图片成功response：" + response);
                //判断头像文件是否上传成功
                JSONObject jsonObject = JSON.parseObject(response);
                if (jsonObject != null && jsonObject.getInteger("code") == 0) {
                    if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                        //请求用户头像更新
                        requestUpLoadImageUpdate(jsonObject.getString("data"));
                        //上传成功后，清空图片路径
                        cirPath = "";
                        return;
                    }
                }
                ToastUtil.showToast(getActivity(), "上传失败，请稍后再试");
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("上传头像上传失败：" + msg);
                ToastUtil.showToast(getActivity(), "上传失败，请稍后再试");
                mLoadingView.hide();
            }
        });
    }


    /**
     * 请求用户头像更新
     */
    private void requestUpLoadImageUpdate(String imageUrl) {
        Request request = NetUrl.getUpDateMeInfoRequest(imageUrl, "");
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("更新头像response：" + response);
                UpdateUserModel updateUserModel = JSON.parseObject(response, new TypeReference<UpdateUserModel>() {
                });
                if (updateUserModel.getCode() == 0) {
                    ToastUtil.showToast(getActivity(), "更新头像成功");
                    //保存昵称数据库
                    DbManager.getInstance().updateMeImage(updateUserModel.getData().getImage());
                    initMeInfo();
                } else {
                    ToastUtil.showToast(getActivity(), "更新头像失败，请稍后再试");
                }
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("更新头像失败：" + msg);
                ToastUtil.showToast(getActivity(), "更新头像失败，请稍后再试");
                mLoadingView.hide();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.i("onActivityResult code：" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SystemHelperManager.REQUEST_PICTURE) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                cirPath = selectList.get(0).getCompressPath();
                if (TextUtils.isEmpty(cirPath)) {
                    ToastUtil.showToast(getActivity(), "图片参数有误");
                    return;
                }
                //开始上传头像
                upLoadCirImage();
            } else if (requestCode == MeInfoActivity.UPDATE_ME_INFO_FLAG) {
                initMeInfo();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
