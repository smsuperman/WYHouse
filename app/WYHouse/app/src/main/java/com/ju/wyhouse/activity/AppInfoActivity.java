package com.ju.wyhouse.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.utils.LogUtil;

public class AppInfoActivity extends BaseBackActivity {

    private TextView app_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        initView();
    }

    private void initView() {
        app_version = findViewById(R.id.app_version);
        app_version.setText(getVerName(this));

    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            LogUtil.e("VersionInfo：" + e);
        }
        return versioncode + "";
    }


    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


}
