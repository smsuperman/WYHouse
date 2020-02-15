package com.ju.wyhouse.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.ju.wyhouse.utils.SystemUI;

/**
 * Author:gaoju
 * Date:2020/1/18 21:30
 * Path:com.ju.wyhouse.base
 * Desc:
 */
public class BaseUiActivity extends BaseActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.fixSystemUI(this);
    }
}
