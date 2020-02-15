package com.ju.wyhouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ju.wyhouse.R;
import com.ju.wyhouse.activity.DiscussActivity;
import com.ju.wyhouse.model.WallDataModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.DataUtil;
import com.ju.wyhouse.utils.GlideUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;

import java.util.List;

import okhttp3.Request;

/**
 * Author:gaoju
 * Date:2020/2/6 17:31
 * Path:com.ju.wyhouse.adapter
 * Desc:墙适配器
 */
public class WallAdapter extends BaseQuickAdapter<WallDataModel, BaseViewHolder> implements LoadMoreModule {

    //背景颜色
    private int[] colorBgWallArray;
    //头像颜色
    private int[] colorCirWallArray;
    private Context mContext;
    private IGoodWallChangeListener iGoodWallChangeListener;
    private LoadingView mLoadingView;
    //判断点赞还是不点赞
    private boolean isLike = false;
    private int wallId;
    private int layoutId;


    public void setiGoodWallChangeListener(IGoodWallChangeListener iGoodWallChangeListener) {
        this.iGoodWallChangeListener = iGoodWallChangeListener;
    }

    public WallAdapter(int layoutResId, List<WallDataModel> data, Context context) {
        super(layoutResId, data);
        mContext = context;
        initAdapter();
    }


    private void initAdapter() {
        colorBgWallArray = DataUtil.getColorBgWallArray();
        colorCirWallArray = DataUtil.getColorCirWallArray();
        mLoadingView = new LoadingView(mContext);
        mLoadingView.setText("请稍等...");
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, WallDataModel wallDataModel) {
        if (wallDataModel.getAnonymous() == 1) {
            //匿名
            baseViewHolder.setImageResource(R.id.item_wall_send_main_cir, colorCirWallArray[wallDataModel.getAvatarColor()]);
        } else if (wallDataModel.getAnonymous() == 0) {
            //不是匿名
            GlideUtil.loadUrl(mContext, wallDataModel.getUser().get(0).getImage(), baseViewHolder.getView(R.id.item_wall_send_main_cir));
        }

        //判断是否需要设置默认背景
        if ((wallDataModel.getBackgroundColor() < 0 || wallDataModel.getBackgroundColor() >= colorBgWallArray.length) && TextUtils.isEmpty(wallDataModel.getBackgroundImage())) {
            GlideUtil.loadUrl(mContext, NetUrl.getBgUrl(), baseViewHolder.getView(R.id.item_wall_send_bg));
        } else if ((wallDataModel.getBackgroundColor() < 0 || wallDataModel.getBackgroundColor() >= colorBgWallArray.length) && !TextUtils.isEmpty(wallDataModel.getBackgroundImage())) {
            //判断是否有设置背景图片
            GlideUtil.loadUrl(mContext, wallDataModel.getBackgroundImage(), baseViewHolder.getView(R.id.item_wall_send_bg));
        } else {
            //直接设置颜色
            baseViewHolder.setImageResource(R.id.item_wall_send_bg, colorBgWallArray[wallDataModel.getBackgroundColor()]);
        }
        //设置id
        baseViewHolder.setText(R.id.item_wall_build_number, "#" + wallDataModel.getId());
        //设置内容
        baseViewHolder.setText(R.id.item_wall_send_text, wallDataModel.getContent());
        //设置点赞数量
        baseViewHolder.setText(R.id.item_wall_good_number, String.valueOf(wallDataModel.getLikeNumber()));
        //设置是否点赞
        baseViewHolder.setImageResource(R.id.item_wall_good, wallDataModel.getLikeId() != 0 ? R.drawable.ic_svg_good_p : R.drawable.ic_svg_good);
        //设置点赞点击事件
        baseViewHolder.getView(R.id.item_wall_good).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallId = wallDataModel.getId();
                layoutId = baseViewHolder.getLayoutPosition();

                if (wallDataModel.getLikeId() == 0) {
                    //没有点赞过，开始点赞 true是点赞
                    isLike = true;
                } else {
                    //点赞过取消点赞
                    isLike = false;
                }
                startLikeOrUnLikeWallNet();
            }
        });
        //设置墙详细内容
        baseViewHolder.setText(R.id.item_wall_comm, String.valueOf(wallDataModel.getMessageNumber()));
        baseViewHolder.getView(R.id.item_wall_comm_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallId = wallDataModel.getId();
                DiscussActivity.startActivity(mContext, wallId, wallDataModel);
            }
        });
        baseViewHolder.getView(R.id.item_wall_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallId = wallDataModel.getId();
                DiscussActivity.startActivity(mContext, wallId, wallDataModel);
            }
        });
    }


    /**
     * 开始点赞
     */
    private void startLikeOrUnLikeWallNet() {
        Request request;
        if (isLike) {
            request = NetUrl.getLikeWallRequest(wallId);
        } else {
            request = NetUrl.getUnLikeWallRequest(wallId);
        }

        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                mLoadingView.hide();
                LogUtil.i("获取结果Response成功：" + response);
                if (JSON.parseObject(response) == null) {
                    ToastUtil.showToast(mContext, "请求失败，请稍后再试");
                    return;
                }

                if (JSON.parseObject(response).getInteger("code") == 0) {
                    if (iGoodWallChangeListener != null) {
                        iGoodWallChangeListener.changeItem(layoutId, isLike);
                        if (isLike) {
                            ToastUtil.showToast(mContext, "点赞成功");
                        } else {
                            ToastUtil.showToast(mContext, "取消点赞成功");
                        }
                    }
                } else {
                    ToastUtil.showToast(mContext, "请求失败，请稍后再试");
                }
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("获取结果Response失败");
                ToastUtil.showToast(mContext, "网络超时，请稍后再试");
                mLoadingView.hide();
            }
        });
    }


    //刷新接口
    public interface IGoodWallChangeListener {
        void changeItem(int position, boolean isLike);
    }
}
