package com.ju.wyhouse.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ju.wyhouse.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/18 11:35
 * Path:com.ju.wyhouse.base
 * Desc: 基础Activity
 */
public class BaseActivity extends AppCompatActivity {


    //申请运行时权限的Code
    private static final int PERMISSION_REQUEST_CODE = 1000;
    //申请窗口权限的Code
    public static final int PERMISSION_WINDOW_REQUEST_CODE = 1001;


    //申明所需权限
    private String[] mStrPermission = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.CAMERA
    };

    //保存还没有进行授权的权限
    private List<String> mPerList = new ArrayList<>();
    //保存进行授权但是没有授权成功的权限
    private List<String> mPerNoList = new ArrayList<>();
    private IOnPermissionResult iOnPermissionResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 调用这个方法开始请求权限，入口
     *
     * @param iOnPermissionResult
     */
    protected void startRequestPermission(IOnPermissionResult iOnPermissionResult) {
        //检查那些权限还没有授权，如果都授权了结果为true
        if (!checkPermissionsAll()) {
            requestAllPermission(iOnPermissionResult);
        } else {
            LogUtil.i("全部权限都已经授权，已检查完毕");
        }
    }


    /**
     * 判断单个权限是否已经授权
     *
     * @param permission
     */
    protected boolean checkSinglePermission(String permission) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int check = checkSelfPermission(permission);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    /**
     * 判断所有权限是否已经授权，没有授权的放入到mPerList
     *
     * @return
     */
    protected boolean checkPermissionsAll() {
        mPerList.clear();
        for (int i = 0; i < mStrPermission.length; i++) {
            boolean check = checkSinglePermission(mStrPermission[i]);
            //如果权限是还没有授权的则放入
            if (!check) {
                mPerList.add(mStrPermission[i]);
            }
        }
        //大于0代表还有一些权限没有得到授权
        return mPerList.size() > 0 ? false : true;
    }


    /**
     * \
     * 请求单个权限,也可以传入数组一次性申请
     */
    protected void requestSingleOrAllPermission(String[] permissionArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArray, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 一次性申请所有权限
     */
    protected void requestAllPermission(IOnPermissionResult iOnPermissionResult) {
        this.iOnPermissionResult = iOnPermissionResult;
        requestSingleOrAllPermission(mPerList.toArray(new String[mPerList.size()]));
    }


    /**
     * 权限结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPerNoList.clear();
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        //被拒绝的权限加到mPerNoList
                        mPerNoList.add(permissions[i]);
                    }
                }

                //返回到回调接口
                if (iOnPermissionResult != null) {
                    if (mPerNoList.size() == 0) {
                        iOnPermissionResult.OnSuccess();
                    } else {
                        iOnPermissionResult.OnFail(mPerNoList);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 权限结果回调
     */
    protected interface IOnPermissionResult {
        void OnSuccess();

        void OnFail(List<String> noPermissions);
    }
}
