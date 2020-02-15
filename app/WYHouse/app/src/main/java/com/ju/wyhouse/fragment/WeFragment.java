package com.ju.wyhouse.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ju.wyhouse.R;
import com.ju.wyhouse.activity.SendWallActivity;
import com.ju.wyhouse.adapter.WallAdapter;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.model.WallDataModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;
import com.ju.wyhouse.view.RecyclerViewLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private FloatingActionButton btn_wall_send;
    private RecyclerView wall_recycler;
    private WallAdapter wallAdapter;
    private List<WallDataModel> mWallDataList = new ArrayList<>();
    private SwipeRefreshLayout wall_refresh;
    //发送墙的标志位
    public static final int SEND_WALL_ACTIVITY = 1000;
    private LoadingView mLoadingView;
    private boolean isFirst = true;
    private int startDataPoint = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_we, container, false);
        initView(view);
        initNet();
        return view;
    }


    /**
     * 获取墙信息
     */
    private void initNet() {
        Request request;
        if (isFirst) {
            isFirst = false;
            request = NetUrl.getWallDataRequest(null);
        } else {
            request = NetUrl.getWallDataRequest(String.valueOf(mWallDataList.get(mWallDataList.size() - 1).getId()));
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
                ToastUtil.showToast(getActivity(), "网络超时，请稍后再试");
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

        if (jsonObject.getInteger("code") != 0) {
            ToastUtil.showToast(getActivity(), "数据解析失败，请稍后再试");
            wallAdapter.getLoadMoreModule().loadMoreFail();
            return;
        }
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            WallDataModel wallDataModel = JSON.parseObject(data.getJSONObject(i).toJSONString(), new TypeReference<WallDataModel>() {
            });
            mWallDataList.add(wallDataModel);
        }

        wallAdapter.notifyItemRangeChanged(startDataPoint, data.size());
        startDataPoint = mWallDataList.size();
        //判读是否还有数据进行加载
        if (data.size() == 0) {
            //全部数据加载完毕
            wallAdapter.getLoadMoreModule().loadMoreEnd();
        } else {
            //数据加载完成
            wallAdapter.getLoadMoreModule().loadMoreComplete();
        }
    }


    private void initView(View view) {
        mLoadingView = new LoadingView(getActivity());
        mLoadingView.setText("加载数据中...");
        btn_wall_send = view.findViewById(R.id.btn_wall_send);
        btn_wall_send.setOnClickListener(this);
        wall_recycler = view.findViewById(R.id.wall_recycler);
        //初始化Recycler，替换成自己写的RecyclerView
        wall_recycler.setLayoutManager(new RecyclerViewLinearLayoutManager(getActivity()));
        wallAdapter = new WallAdapter(R.layout.item_wall, mWallDataList, getActivity());
        //设置加载更多监听，先关闭
        wallAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        wallAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> initNet());
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
        wall_refresh = view.findViewById(R.id.wall_refresh);
        wall_refresh.setOnRefreshListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wall_send:
                startActivityForResult(new Intent(getContext(), SendWallActivity.class), SEND_WALL_ACTIVITY);
                break;
            default:
                break;
        }
    }

    //监听下拉刷新
    @Override
    public void onRefresh() {
        mWallDataList.clear();
        wallAdapter.notifyDataSetChanged();
        isFirst = true;
        initNet();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.i("WeFragment resultCode:" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SEND_WALL_ACTIVITY) {
                //清空列表
                mWallDataList.clear();
                wallAdapter.notifyDataSetChanged();
                isFirst = true;
                initNet();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
