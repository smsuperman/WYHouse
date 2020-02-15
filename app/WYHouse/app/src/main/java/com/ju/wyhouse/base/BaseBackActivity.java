package com.ju.wyhouse.base;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author:gaoju
 * Date:2020/1/18 15:29
 * Path:com.ju.wyhouse.base
 * Desc:
 */
public class BaseBackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //清除阴影
        getSupportActionBar().setElevation(0);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
