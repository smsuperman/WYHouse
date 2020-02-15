package com.ju.wyhouse.service;

import android.content.Context;
import android.text.TextUtils;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.utils.LogUtil;

/**
 * Author:gaoju
 * Date:2020/1/19 13:51
 * Path:com.ju.wyhouse.service
 * Desc: 个推服务接收类
 */
public class PushIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {

    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        LogUtil.i("onReceiveMessageData -> " + "msg = " + msg);
        LogUtil.i("onReceiveMessageData -> " + "msg = " + msg.getMessageId());
        LogUtil.i("onReceiveMessageData -> " + "msg = " + msg.getAppid());
        LogUtil.i("onReceiveMessageData -> " + "msg = " + msg.getTaskId());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        //这里接收收到的内容和消息
        LogUtil.i("onNotificationMessageArrived -> " + "msg = " + msg.getTitle());
        LogUtil.i("onNotificationMessageArrived -> " + "msg = " + msg.getContent());
        LogUtil.i("onNotificationMessageArrived -> " + "msg = " + msg.getMessageId());

        String messageId = msg.getMessageId();
        String title = msg.getTitle();
        String content = msg.getContent();
        //如果有一个为空就return，否则存进数据库
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(messageId)) {
            return;
        }
        //保存到数据库
        DbManager.getInstance().saveMessage(messageId,title,content);
        LogUtil.i("消息已存进数据库");

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        LogUtil.i("消息被点击了");
    }
}
