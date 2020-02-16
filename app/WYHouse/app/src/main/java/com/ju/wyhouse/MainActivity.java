package com.ju.wyhouse;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ju.wyhouse.base.BaseUiActivity;
import com.ju.wyhouse.fragment.MainFragment;
import com.ju.wyhouse.fragment.MeFragment;
import com.ju.wyhouse.fragment.WeFragment;
import com.ju.wyhouse.service.PushIntentService;
import com.ju.wyhouse.service.PushService;
import com.ju.wyhouse.utils.LogUtil;
import com.tencent.bugly.Bugly;

import java.util.List;

public class MainActivity extends BaseUiActivity implements View.OnClickListener {


    //控件初始化
    private FrameLayout mainFrameLayout;
    private ImageView main_iv_main;
    private TextView main_tv_main;
    private LinearLayout main_ll_main;

    private ImageView main_iv_util;
    private TextView main_tv_util;
    private LinearLayout main_ll_util;

    private ImageView main_iv_me;
    private TextView main_tv_me;
    private LinearLayout main_ll_me;


    //Frangment相关
    private MainFragment mMainFragment;
    private WeFragment mWeFragment;
    private MeFragment mMeFragment;
    //事务
    private FragmentTransaction mMainTransaction;
    private FragmentTransaction mUtilTransaction;
    private FragmentTransaction mMeTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //开始先判断权限
        requestCheck();
        initView();
        initFragment();
        //默认是0
        switchTab(0);
        initOtherMain();
    }

    /**
     * 初始化一些其它框架
     */
    private void initOtherMain() {
        //个推初始化
        com.igexin.sdk.PushManager.getInstance().initialize(getApplicationContext(), PushService.class);
        //注册个推服务
        com.igexin.sdk.PushManager.getInstance().registerPushIntentService(getApplicationContext(), PushIntentService.class);
        //Bugly应用升级初始化
        Bugly.init(getApplicationContext(), "51170706f6", false);
    }


    /**
     * 切换Fragment，默认是0
     */
    private void switchTab(int index) {
        switch (index) {
            case 0:
                showFragment(mMainFragment);
                main_iv_main.setImageResource(R.drawable.img_square_p);
                main_iv_util.setImageResource(R.drawable.img_chat);
                main_iv_me.setImageResource(R.drawable.img_me);

                main_tv_main.setTextColor(getResources().getColor(R.color.colorAccent));
                main_tv_util.setTextColor(Color.BLACK);
                main_tv_me.setTextColor(Color.BLACK);
                break;
            case 1:
                showFragment(mWeFragment);
                main_iv_main.setImageResource(R.drawable.img_square);
                main_iv_util.setImageResource(R.drawable.img_chat_p);
                main_iv_me.setImageResource(R.drawable.img_me);

                main_tv_main.setTextColor(Color.BLACK);
                main_tv_util.setTextColor(getResources().getColor(R.color.colorAccent));
                main_tv_me.setTextColor(Color.BLACK);
                break;
            case 2:
                showFragment(mMeFragment);
                main_iv_main.setImageResource(R.drawable.img_square);
                main_iv_util.setImageResource(R.drawable.img_chat);
                main_iv_me.setImageResource(R.drawable.img_me_p);

                main_tv_main.setTextColor(Color.BLACK);
                main_tv_util.setTextColor(Color.BLACK);
                main_tv_me.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            default:
                break;
        }
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {

        //主页Fragment
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            mMainTransaction = getSupportFragmentManager().beginTransaction();
            mMainTransaction.add(R.id.mainFrameLayout, mMainFragment);
            mMainTransaction.commit();
        }

        //主页Fragment
        if (mWeFragment == null) {
            mWeFragment = new WeFragment();
            mUtilTransaction = getSupportFragmentManager().beginTransaction();
            mUtilTransaction.add(R.id.mainFrameLayout, mWeFragment);
            mUtilTransaction.commit();
        }

        //主页Fragment
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mainFrameLayout, mMeFragment);
            mMeTransaction.commit();
        }
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mainFrameLayout = findViewById(R.id.mainFrameLayout);
        main_iv_main = findViewById(R.id.main_iv_main);
        main_tv_main = findViewById(R.id.main_tv_main);
        main_ll_main = findViewById(R.id.main_ll_main);
        main_iv_util = findViewById(R.id.main_iv_util);
        main_tv_util = findViewById(R.id.main_tv_util);
        main_ll_util = findViewById(R.id.main_ll_util);
        main_iv_me = findViewById(R.id.main_iv_me);
        main_tv_me = findViewById(R.id.main_tv_me);
        main_ll_me = findViewById(R.id.main_ll_me);
        //初始化点击事件
        main_ll_main.setOnClickListener(this);
        main_ll_util.setOnClickListener(this);
        main_ll_me.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_ll_main:
                switchTab(0);
                break;
            case R.id.main_ll_util:
                switchTab(1);
                break;
            case R.id.main_ll_me:
                switchTab(2);
                break;
            default:
                break;
        }
    }

    /**
     * 展示Fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //先隐藏全部Fragment
            hideAllFragment(transaction);
            //然后在现实需要的Fragment
            transaction.show(fragment);
            transaction.commit();
        }
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mMainFragment != null) {
            transaction.hide(mMainFragment);
        }
        if (mWeFragment != null) {
            transaction.hide(mWeFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }

    }

    /**
     * 防止重叠
     * 当应用的内存紧张的时候，系统会回收掉Fragment对象
     * 再一次进入的时候会重新创建Fragment
     * 非原来对象，我们无法控制，导致重叠
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (mMainFragment == null && fragment instanceof MainFragment) {
            mMainFragment = (MainFragment) fragment;
        }
        if (mWeFragment == null && fragment instanceof WeFragment) {
            mWeFragment = (WeFragment) fragment;
        }
        if (mMeFragment == null && fragment instanceof MeFragment) {
            mMeFragment = (MeFragment) fragment;
        }
    }


    /**
     * 权限检查
     */
    private void requestCheck() {
        startRequestPermission(new IOnPermissionResult() {
            @Override
            public void OnSuccess() {
                LogUtil.i("全部权限请求已通过");
            }

            @Override
            public void OnFail(List<String> noPermissions) {
                LogUtil.i("以下权限未通过：" + noPermissions.toString());
            }
        });
    }



}
