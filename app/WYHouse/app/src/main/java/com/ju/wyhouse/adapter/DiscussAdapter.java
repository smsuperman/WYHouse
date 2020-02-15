package com.ju.wyhouse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ju.wyhouse.R;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.model.DiscussModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.HttpRequest;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.GlideUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.DialogView;

import java.util.List;

import okhttp3.Request;

/**
 * Author:gaoju
 * Date:2020/2/10 18:42
 * Path:com.ju.wyhouse.adapter
 * Desc:评论适配器
 */
public class DiscussAdapter extends BaseQuickAdapter<DiscussModel.DataBean, BaseViewHolder> implements LoadMoreModule {


    private Context mContext;
    //自己的id
    private int meId;
    private DialogView dialogView;
    private TextView double_dialog_title;
    private TextView double_dialog_message;
    private Button double_dialog_no;
    private Button double_dialog_yes;
    private String flagId;
    private int wallId;
    private int discussId;
    private int layoutId;
    private IDiscussRemoveListener iDiscussRemoveListener;


    public DiscussAdapter(Context context, int wallId, int layoutResId, List<DiscussModel.DataBean> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.wallId = wallId;
        initView();
    }


    public void setiDiscussRemoveListener(IDiscussRemoveListener iDiscussRemoveListener) {
        this.iDiscussRemoveListener = iDiscussRemoveListener;
    }

    private void initView() {
        meId = DbManager.getInstance().queryMeUser().getUserId();
        flagId = SaveAndWriteUtil.getFlagId();
        dialogView = DialogManager.getInstance().initView(mContext, R.layout.dialog_double_result, Gravity.BOTTOM);
        double_dialog_title = dialogView.findViewById(R.id.double_dialog_title);
        double_dialog_title.setText("系统提示");
        double_dialog_message = dialogView.findViewById(R.id.double_dialog_message);
        double_dialog_message.setText("确定删除这条评论吗？");
        double_dialog_no = dialogView.findViewById(R.id.double_dialog_no);
        double_dialog_no.setText("取消");
        double_dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(dialogView);
            }
        });
        double_dialog_yes = dialogView.findViewById(R.id.double_dialog_yes);
        double_dialog_yes.setText("确定");
        double_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDelDiscuss();
                DialogManager.getInstance().hide(dialogView);
            }
        });
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, DiscussModel.DataBean dataBean) {
        //设置头像
        GlideUtil.loadUrl(mContext, dataBean.getUser().getImage(), baseViewHolder.getView(R.id.item_discuss_cir));
        //设置昵称
        baseViewHolder.setText(R.id.item_discuss_nickName, dataBean.getUser().getName());
        //设置内容
        baseViewHolder.setText(R.id.item_discuss_content, dataBean.getContent());
        //设置时间
        baseViewHolder.setText(R.id.item_discuss_time, dataBean.getCreateTime());
        //设置垃圾桶
        if (meId == dataBean.getUser().getId()) {
            baseViewHolder.getView(R.id.item_discuss_rubbish).setVisibility(View.VISIBLE);
            //设置监听事件
            baseViewHolder.getView(R.id.item_discuss_rubbish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutId = baseViewHolder.getLayoutPosition();
                    discussId = dataBean.getCommentId();
                    dialogView.show();
                }
            });
        } else if (meId != dataBean.getUser().getId()) {
            baseViewHolder.getView(R.id.item_discuss_rubbish).setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 删除评论请求
     */
    private void requestDelDiscuss() {
        Request request = NetUrl.getDelWallDiscussRequest(wallId, discussId);
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("请求删除Response成功：" + response);
                parsingDelDiscussJson(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("删除评论失败：" + msg);
                ToastUtil.showToast(mContext, "网络超时，请稍后再试");
            }
        });
    }


    /**
     * 解析判断
     *
     * @param response
     */
    private void parsingDelDiscussJson(String response) {
        if (JSON.parseObject(response).getInteger("code") == 0) {
            ToastUtil.showToast(mContext, "删除评论成功");
            if (iDiscussRemoveListener != null) {
                iDiscussRemoveListener.discussRemoveChange(layoutId);
            }
        } else {
            ToastUtil.showToast(mContext, "删除失败，请稍后再试");
        }
    }


    public interface IDiscussRemoveListener {
        void discussRemoveChange(int position);
    }
}
