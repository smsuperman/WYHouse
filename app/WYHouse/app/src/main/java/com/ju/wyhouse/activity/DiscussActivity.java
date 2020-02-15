package com.ju.wyhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.ju.wyhouse.R;
import com.ju.wyhouse.adapter.DiscussAdapter;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.model.DiscussModel;
import com.ju.wyhouse.model.DiscussResultModel;
import com.ju.wyhouse.model.WallDataModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.HttpRequest;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.DataUtil;
import com.ju.wyhouse.utils.GlideUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.RecyclerViewLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;

public class DiscussActivity extends BaseBackActivity implements View.OnClickListener {


    private ImageView discuss_empty_view;
    private ImageView discuss_wall_send_bg;
    private TextView discuss_wall_send_text;
    private CircleImageView discuss_wall_send_main_cir;
    private RecyclerView discuss_recycler;
    private int wallId;
    private int lastId;
    private final static String WALL_ID = "wall_id";
    private final static String MODEL_FLAG = "model_flag";
    private boolean isFirst = true;
    private List<DiscussModel.DataBean> discussModelDataList = new ArrayList<>();
    private DiscussAdapter mDiscussAdapter;
    //刷新的下标
    private int dataSize = 0;
    private WallDataModel wallDataModel;
    //背景颜色
    private int[] colorBgWallArray;
    //头像颜色
    private int[] colorCirWallArray;
    private ImageView discuss_send_text;
    private EditText discuss_ed_input_msg;


    public static void startActivity(Context context, int wallId, WallDataModel wallDataModel) {
        Intent intent = new Intent(context, DiscussActivity.class);
        intent.putExtra(WALL_ID, wallId);
        intent.putExtra(MODEL_FLAG, wallDataModel);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        initData();
        initView();
        updateWall();
        requestDiscussList();
    }

    private void initData() {
        colorBgWallArray = DataUtil.getColorBgWallArray();
        colorCirWallArray = DataUtil.getColorCirWallArray();
        wallId = getIntent().getIntExtra(WALL_ID, 0);
        wallDataModel = (WallDataModel) getIntent().getSerializableExtra(MODEL_FLAG);
    }


    /**
     * 请求评论数据
     */
    private void requestDiscussList() {
        Request request;
        if (isFirst) {
            request = NetUrl.getWallDiscussRequest(wallId, "");
            isFirst = false;
        } else {
            request = NetUrl.getWallDiscussRequest(wallId, String.valueOf(lastId));
        }


        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取墙评论网络请求成功：" + response);
                parsingDiscussData(response);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(DiscussActivity.this, "网络超时，请稍后再试");
                LogUtil.e("获取墙评论网络请求错误：" + msg);
                mDiscussAdapter.getLoadMoreModule().loadMoreFail();
            }
        });

    }


    /**
     * 解析墙评论数据
     *
     * @param response
     */
    private void parsingDiscussData(String response) {
        DiscussModel discussModel = JSON.parseObject(response, new TypeReference<DiscussModel>() {
        });

        if (discussModel.getCode() != 0) {
            ToastUtil.showToast(this, "加载错误,请稍后再试");
            mDiscussAdapter.getLoadMoreModule().loadMoreFail();
            return;
        }

        discussModelDataList.addAll(discussModel.getData());
        mDiscussAdapter.notifyItemRangeChanged(dataSize, discussModel.getData().size());
        dataSize = discussModelDataList.size();
        checkRecycler();
        //判读是否还有数据进行加载
        if (discussModel.getData() != null && discussModel.getData().size() > 0) {
            //数据加载完成
            mDiscussAdapter.getLoadMoreModule().loadMoreComplete();
            lastId = discussModel.getData().get(discussModel.getData().size() - 1).getCommentId();
        } else {
            //全部数据加载完毕
            mDiscussAdapter.getLoadMoreModule().loadMoreEnd();
        }
    }

    /**
     * 判断显示空view还是评论
     */
    private void checkRecycler() {
        if (discussModelDataList.size() > 0) {
            discuss_empty_view.setVisibility(View.GONE);
            discuss_recycler.setVisibility(View.VISIBLE);
        } else {
            discuss_empty_view.setVisibility(View.VISIBLE);
            discuss_recycler.setVisibility(View.GONE);
        }
    }

    private void initView() {
        getSupportActionBar().setTitle("墙评论");
        discuss_empty_view = findViewById(R.id.discuss_empty_view);
        discuss_wall_send_bg = findViewById(R.id.discuss_wall_send_bg);
        discuss_wall_send_text = findViewById(R.id.discuss_wall_send_text);
        discuss_wall_send_main_cir = findViewById(R.id.discuss_wall_send_main_cir);
        discuss_recycler = findViewById(R.id.discuss_recycler);
        discuss_ed_input_msg = findViewById(R.id.discuss_ed_input_msg);
        discuss_send_text = findViewById(R.id.discuss_send_text);
        discuss_send_text.setOnClickListener(this);
        //初始化adpater
        discuss_recycler.setLayoutManager(new RecyclerViewLinearLayoutManager(this));
        mDiscussAdapter = new DiscussAdapter(this, wallId, R.layout.item_discuss, discussModelDataList);
        discuss_recycler.setAdapter(mDiscussAdapter);
        //上拉加载监听
        mDiscussAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestDiscussList();
            }
        });
        //删除评论监听
        mDiscussAdapter.setiDiscussRemoveListener(new DiscussAdapter.IDiscussRemoveListener() {
            @Override
            public void discussRemoveChange(int position) {
                //删除item和数据
                discussModelDataList.remove(position);
                mDiscussAdapter.notifyItemRemoved(position);
                checkRecycler();
            }
        });
    }


    private void updateWall() {
        //设置头像
        if (wallDataModel.getAnonymous() == 1) {
            discuss_wall_send_main_cir.setImageResource(colorCirWallArray[wallDataModel.getAvatarColor()]);
        } else {
            GlideUtil.loadUrl(this, wallDataModel.getUser().get(0).getImage(), discuss_wall_send_main_cir);
        }

        //设置背景
        if ((wallDataModel.getBackgroundColor() < 0 || wallDataModel.getBackgroundColor() >= colorBgWallArray.length) && TextUtils.isEmpty(wallDataModel.getBackgroundImage())) {
            GlideUtil.loadUrl(this, NetUrl.getBgUrl(), discuss_wall_send_bg);
        } else if ((wallDataModel.getBackgroundColor() < 0 || wallDataModel.getBackgroundColor() >= colorBgWallArray.length) && !TextUtils.isEmpty(wallDataModel.getBackgroundImage())) {
            //判断是否有设置背景图片
            GlideUtil.loadUrl(this, wallDataModel.getBackgroundImage(), discuss_wall_send_bg);
        } else {
            //直接设置颜色
            discuss_wall_send_bg.setImageResource(colorBgWallArray[wallDataModel.getBackgroundColor()]);
        }
        //设置内容
        discuss_wall_send_text.setText(wallDataModel.getContent());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discuss_send_text:
                startInputDiscuss();
                break;
            default:
                break;
        }
    }

    private void startInputDiscuss() {
        if (TextUtils.isEmpty(discuss_ed_input_msg.getText().toString())) {
            ToastUtil.showToast(this, "评论内容不能为空！");
            return;
        }
        Request request = NetUrl.getWallDiscussSendRequest(wallId, discuss_ed_input_msg.getText().toString());
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("发送评论成功response：" + response);
                parsingDiscussJson(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("发送评论错误：" + msg);
                ToastUtil.showToast(DiscussActivity.this, "网络超时，请稍后再试");
            }
        });

    }


    private void parsingDiscussJson(String response) {
        DiscussResultModel discussResultModel = JSON.parseObject(response, new TypeReference<DiscussResultModel>() {
        });
        if (discussResultModel.getCode() == 0) {
            if (discussResultModel.getData() != null) {
                ToastUtil.showToast(this, "发送成功");
                discuss_ed_input_msg.setText("");
                requestDiscussList();
            }
        } else {
            ToastUtil.showToast(this, "发送失败，请稍后再试");
        }
    }
}
