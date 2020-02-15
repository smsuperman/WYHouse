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

public class WallActivity extends BaseUiActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionButton btn_wall_send;
    private RecyclerView wall_recycler;
    private WallAdapter wallAdapter;
    private List<WallDataModel> mWallDataList = new ArrayList<>();
    private int lastId;
    private SwipeRefreshLayout wall_refresh;
    //发送墙的标志位
    public static final int SEND_WALL_ACTIVITY = 1000;
    private LoadingView mLoadingView;
    private boolean isFirst = true;
    private int size = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        initView();
        initNet();
    }


    /**
     * 首次获取墙信息
     */
    private void initNet() {
        Request request = NetUrl.getWallDataRequest(null);
        if (isFirst) {
            isFirst = false;
            mLoadingView.show();
        }

        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取墙数据response成功：" + response);
                parsingWallData(response);
                wall_refresh.setRefreshing(false);
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("获取墙数据网络请求错误：" + msg);
                ToastUtil.showToast(WallActivity.this, "网络超时，请稍后再试");
                wall_refresh.setRefreshing(false);
                mLoadingView.hide();
            }
        });
    }


    /**
     * 解析数据
     *
     * @param response
     */
    private void parsingWallData(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray data = jsonObject.getJSONArray("data");

        if (jsonObject.getInteger("code") != 0) {
            ToastUtil.showToast(this, "数据解析失败，请稍后再试");
            wallAdapter.getLoadMoreModule().loadMoreFail();
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            WallDataModel wallDataModel = JSON.parseObject(data.getJSONObject(i).toJSONString(), new TypeReference<WallDataModel>() {
            });
            mWallDataList.add(wallDataModel);
        }

        wallAdapter.notifyItemRangeChanged(size, 10);
        size = mWallDataList.size();
        //判读是否还有数据进行加载
        if (mWallDataList.get(mWallDataList.size() - 1).getId() == 1) {
            //全部数据加载完毕
            wallAdapter.getLoadMoreModule().loadMoreEnd();
        } else {
            //数据加载完成
            wallAdapter.getLoadMoreModule().loadMoreComplete();
        }
    }


    private void initView() {
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("加载数据中...");
        btn_wall_send = findViewById(R.id.btn_wall_send);
        btn_wall_send.setOnClickListener(this);
        wall_recycler = findViewById(R.id.wall_recycler);
        //初始化Recycler，替换成自己写的RecyclerView
        wall_recycler.setLayoutManager(new RecyclerViewLinearLayoutManager(this));
        wallAdapter = new WallAdapter(R.layout.item_wall, mWallDataList, this);
        //设置加载更多监听，先关闭
        wallAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        wallAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> loadMoreWallData());
        wall_recycler.setAdapter(wallAdapter);
        //去除局部刷新闪烁
        ((DefaultItemAnimator) wall_recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        //设置刷新数据监听
        wallAdapter.setiGoodWallChangeListener((position, isLike) -> {
            if (isLike) {
                mWallDataList.get(position).setLikeNumber(mWallDataList.get(position).getLikeNumber() + 1);
                mWallDataList.get(position).setLikeId(DbManager.getInstance().queryMeUser().getUserId());
            } else {
                mWallDataList.get(position).setLikeNumber(mWallDataList.get(position).getLikeNumber() - 1);
                mWallDataList.get(position).setLikeId(0);
            }
            wallAdapter.notifyItemChanged(position);
        });

        //下拉刷新初始化
        wall_refresh = findViewById(R.id.wall_refresh);
        wall_refresh.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wall_send:
                startActivityForResult(new Intent(this, SendWallActivity.class), SEND_WALL_ACTIVITY);
                break;
            default:
                break;
        }
    }


    /**
     * 加载更多数据
     */
    private void loadMoreWallData() {
        if (mWallDataList.size() > 0) {
            lastId = mWallDataList.get(mWallDataList.size() - 1).getId();
        }
        Request request = NetUrl.getWallDataRequest(String.valueOf(lastId));
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取墙数据response成功：" + response);
                parsingWallData(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("获取墙数据网络请求错误：" + msg);
                ToastUtil.showToast(WallActivity.this, "网络超时，请稍后再试");
                wallAdapter.getLoadMoreModule().loadMoreFail();
            }
        });

    }


    //监听下拉刷新
    @Override
    public void onRefresh() {
        mWallDataList.clear();
        wallAdapter.notifyDataSetChanged();
        initNet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.i("WallActivity resultCode:" + resultCode);
        if (resultCode == SEND_WALL_ACTIVITY) {
            //清空列表
            mWallDataList.clear();
            wallAdapter.notifyDataSetChanged();
            initNet();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
