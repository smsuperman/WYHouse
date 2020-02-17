package com.ju.wyhouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ju.wyhouse.R;
import com.ju.wyhouse.adapter.WallAdapter;
import com.ju.wyhouse.base.BaseUiActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.model.WallDataModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;
import com.ju.wyhouse.view.RecyclerViewLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class WallActivity extends BaseUiActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

    }


}
