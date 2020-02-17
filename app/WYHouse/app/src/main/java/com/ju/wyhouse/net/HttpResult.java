package com.ju.wyhouse.net;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.ju.wyhouse.entity.Constants;

/**
 * Author:gaoju
 * Date:2020/1/18 14:03
 * Path:com.ju.wyhouse.net
 * Desc: 网络请求返回结果类
 */
public class HttpResult {

    private INetCallback iNetCallback;
    private int requestCode;
    private String requestRes;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.arg1) {
                case Constants.REQUEST_SUCCESS:
                    iNetCallback.onSuccess(msg.obj.toString());
                    break;
                case Constants.REQUEST_FAILURE:
                    iNetCallback.onFailure(msg.obj.toString());
                    break;
                case Constants.REQUEST_ERROR:
                    iNetCallback.onError(msg.obj.toString());
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    public void startNetResult(int code, String res, INetCallback callback) {
        this.requestCode = code;
        this.requestRes = res;
        this.iNetCallback = callback;
        createMessage();
    }

    //创建Message
    private void createMessage() {
        Message message = new Message();
        message.arg1 = requestCode;
        message.obj = requestRes;
        mHandler.sendMessage(message);
    }


}
