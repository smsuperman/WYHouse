package com.ju.wyhouse.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ju.wyhouse.R;
import com.ju.wyhouse.adapter.CommonAdapter;
import com.ju.wyhouse.adapter.CommonViewHolder;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.manager.SystemHelperManager;
import com.ju.wyhouse.model.ColorWallModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.DataUtil;
import com.ju.wyhouse.utils.GlideUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;


/**
 * 墙发送
 */
public class SendWallActivity extends BaseBackActivity implements View.OnClickListener {

    //Model类型
    private static final int WALL_COLOR_TYPE = 0;
    private static final int WALL_CAMERA_TYPE = 1;
    private List<ColorWallModel> colorWallList = new ArrayList<>();
    private CommonAdapter<ColorWallModel> colorBgWallAdapter;
    private CommonAdapter<Integer> colorCirWallAdapter;
    private RecyclerView recycler_wall_bg;
    private RecyclerView recycler_wall_cir;
    //背景颜色
    private int[] colorBgWallArray;
    //头像颜色
    private int[] colorCirWallArray;
    private List<Integer> colorCirWallList = new ArrayList<>();
    private ImageView wall_send_bg;
    private TextView wall_send_text;
    private TextView send_wall_tv_bg;
    private TextView send_wall_tv_cir;
    private Switch sw_cir_name;
    private CircleImageView wall_send_main_cir;
    private EditText send_wall_input_text;
    private Button btn_send_wall;
    //图片路径
    private String bgFilePath;
    private LoadingView mLoadingView;
    private String imageUrl;
    private int bgColor = NO_CIR_COLOR_FLAG;
    //头像颜色
    private int cirColor = NO_CIR_COLOR_FLAG;
    //没有设置颜色的标志位
    private static final int NO_CIR_COLOR_FLAG = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_wall);
        initData();
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("上墙");
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("正在发送中...");
        recycler_wall_bg = findViewById(R.id.recycler_wall_bg);
        recycler_wall_cir = findViewById(R.id.recycler_wall_cir);
        wall_send_bg = findViewById(R.id.wall_send_bg);
        //获取默认背景
        GlideUtil.loadUrl(this, NetUrl.getBgUrl(), wall_send_bg);
        wall_send_text = findViewById(R.id.wall_send_text);
        sw_cir_name = findViewById(R.id.sw_cir_name);
        wall_send_main_cir = findViewById(R.id.wall_send_main_cir);
        send_wall_input_text = findViewById(R.id.send_wall_input_text);
        btn_send_wall = findViewById(R.id.btn_send_wall);
        btn_send_wall.setOnClickListener(this);
        //初始化背景和头像选择
        send_wall_tv_bg = findViewById(R.id.send_wall_tv_bg);
        send_wall_tv_cir = findViewById(R.id.send_wall_tv_cir);
        send_wall_tv_bg.setOnClickListener(this);
        send_wall_tv_cir.setOnClickListener(this);
        //初始化背景Recycler
        recycler_wall_bg.setLayoutManager(new GridLayoutManager(this, 6));
        colorBgWallAdapter = new CommonAdapter<>(colorWallList, new CommonAdapter.OnMoreBindDataListener<ColorWallModel>() {
            @Override
            public int getItemType(int position) {
                return colorWallList.get(position).getType();
            }

            @Override
            public void onBindViewHolder(ColorWallModel model, CommonViewHolder viewHolder, int type, int position) {
                if (type == WALL_COLOR_TYPE) {
                    viewHolder.setImageResource(R.id.send_wall_cir, colorWallList.get(position).getColor());
                    viewHolder.getView(R.id.send_wall_cir).setOnClickListener(v -> {
                        //设置背景颜色
                        bgColor = position;
                        //清空选择相册路径
                        bgFilePath = "";
                        wall_send_bg.setImageResource(colorWallList.get(position).getColor());
                    });
                } else if (type == WALL_CAMERA_TYPE) {
                    //设置图库按钮
                    viewHolder.setImageResource(R.id.send_wall_cir, R.drawable.img_wall_camera_png);
                    viewHolder.getView(R.id.send_wall_cir).setOnClickListener(v -> {
                        //选择图片
                        SystemHelperManager.getInstance().openPictureSelectorBg(SendWallActivity.this);
                    });
                }
            }

            @Override
            public int getLayout(int type) {
                return R.layout.item_color_send_wall;
            }
        });
        recycler_wall_bg.setAdapter(colorBgWallAdapter);
        //初始化头像Recycler
        recycler_wall_cir.setLayoutManager(new GridLayoutManager(this, 6));
        colorCirWallAdapter = new CommonAdapter<>(colorCirWallList, new CommonAdapter.OnBindDataListener<Integer>() {
            @Override
            public void onBindViewHolder(Integer model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.setImageResource(R.id.send_wall_cir, colorCirWallList.get(position));
                viewHolder.getView(R.id.send_wall_cir).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!sw_cir_name.isChecked()) {
                            ToastUtil.showToast(SendWallActivity.this, "需要开启匿名才能设置头像");
                            return;
                        }
                        cirColor = position;
                        wall_send_main_cir.setImageResource(colorCirWallList.get(cirColor));
                    }
                });
            }

            @Override
            public int getLayout(int type) {
                return R.layout.item_color_send_wall;
            }
        });
        recycler_wall_cir.setAdapter(colorCirWallAdapter);
        //监听匿名控件状态
        sw_cir_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选中后默认设置第一个颜色
                    cirColor = 0;
                    wall_send_main_cir.setImageResource(colorCirWallList.get(cirColor));
                } else {
                    //设置原本头像头像
                    wall_send_main_cir.setImageResource(R.mipmap.ic_logo);
                    //设置标志位
                    cirColor = NO_CIR_COLOR_FLAG;
                }
            }
        });
        //设置输入监听
        send_wall_input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wall_send_text.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void initData() {
        //获取颜色数据
        colorBgWallArray = DataUtil.getColorBgWallArray();
        colorCirWallArray = DataUtil.getColorCirWallArray();

        for (int i = 0; i < colorBgWallArray.length; i++) {
            ColorWallModel colorWallModel = new ColorWallModel();
            colorWallModel.setType(WALL_COLOR_TYPE);
            colorWallModel.setColor(colorBgWallArray[i]);
            colorWallList.add(colorWallModel);
        }
        //添加添加图片Model
        ColorWallModel colorWallModel = new ColorWallModel();
        colorWallModel.setType(WALL_CAMERA_TYPE);
        colorWallList.add(colorWallModel);

        //初始化头像颜色
        for (Integer cirColor : colorCirWallArray) {
            colorCirWallList.add(cirColor);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_wall_tv_bg:
                send_wall_tv_bg.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                send_wall_tv_cir.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                recycler_wall_bg.setVisibility(View.VISIBLE);
                recycler_wall_cir.setVisibility(View.GONE);
                break;
            case R.id.send_wall_tv_cir:
                send_wall_tv_bg.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                send_wall_tv_cir.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                recycler_wall_bg.setVisibility(View.GONE);
                recycler_wall_cir.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_send_wall:
                //发送
                startSendWall();
                break;
            default:
                break;
        }
    }


    /**
     * 开始上传
     */
    private void startSendWall() {
        if (TextUtils.isEmpty(send_wall_input_text.getText())) {
            ToastUtil.showToast(this, "请输入内容！");
            return;
        }
        mLoadingView.show();
        //判断要不要上传背景图片
        if (TextUtils.isEmpty(bgFilePath)) {
            //空的直接开始上传内容
            startToContent();
        } else {
            startToImage();
        }
    }


    /**
     * 开始上传图片
     */
    private void startToImage() {
        File file = new File(bgFilePath);
        Request request = NetUrl.getUpLoadImageFile(file);
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("上墙图片成功response：" + response);
                parsingImage(response);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(SendWallActivity.this, msg);
                mLoadingView.hide();
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(SendWallActivity.this, "网络超时，请稍后再试");
            }
        });
    }


    /**
     * 解析获得上传图片的地址
     *
     * @param response
     */
    private void parsingImage(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        if (jsonObject.getInteger("code") != 0) {
            ToastUtil.showToast(this, "发布失败，请稍后再试");
            mLoadingView.hide();
            return;
        }
        imageUrl = jsonObject.getString("data");
        //获得图片的地址后开始上传内容
        startToContent();
    }


    //开始上传内容
    private void startToContent() {
        Request request = null;
        //获取是否匿名
        int anony = sw_cir_name.isChecked() ? 1 : 0;
        //获取内容
        String content = send_wall_input_text.getText().toString();
        if (bgColor != NO_CIR_COLOR_FLAG) {
            //设置背景颜色 不自定义图片
            request = NetUrl.getSendWallRequest(content, anony, bgColor, cirColor, "");
        } else if (!TextUtils.isEmpty(imageUrl)) {
            //设置自定义图片，传自定义图片
            request = NetUrl.getSendWallRequest(content, anony, bgColor, cirColor, imageUrl);
        } else if (bgColor == NO_CIR_COLOR_FLAG) {
            //不设置背景颜色也不设置自定义图片，传默认图片
            request = NetUrl.getSendWallRequest(content, anony, bgColor, cirColor, NetUrl.getBgUrl());
        }
        //开始上传内容
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("发布内容成功Response：" + response);
                parsingContent(response);
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(SendWallActivity.this, msg);
                mLoadingView.hide();
            }


            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(SendWallActivity.this, "网络超时，请稍后再试");
            }
        });
    }

    /**
     * 解析内容是否发送成功
     *
     * @param response
     */
    private void parsingContent(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        if (jsonObject.getInteger("code") == 0) {
            ToastUtil.showToast(SendWallActivity.this, "上墙成功");
            //设置标志位
            setResult(RESULT_OK);
            finish();
        } else {
            ToastUtil.showToast(SendWallActivity.this, "发布失败，请稍后再试");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.i("resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == SystemHelperManager.REQUEST_PICTURE) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                bgFilePath = selectList.get(0).getCompressPath();
                if (TextUtils.isEmpty(bgFilePath)) {
                    ToastUtil.showToast(SendWallActivity.this, "图片参数有误");
                    return;
                }
                GlideUtil.loadFile(this, new File(bgFilePath), wall_send_bg);
                //设置完图片清空背景颜色设置
                bgColor = NO_CIR_COLOR_FLAG;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
