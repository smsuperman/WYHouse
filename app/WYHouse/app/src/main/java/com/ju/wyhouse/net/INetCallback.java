package com.ju.wyhouse.net;

/**
 * 项目名： HellloZAndroid
 * 包名 com.ju.helllozandroid.net
 * 文件名： INetCallback
 * 创建日期：2019/9/20
 * 描述：请求回调封装包括，包括加载前后加载后，
 */
public interface INetCallback {


    void onSuccess(String response);

    void onFailure(String msg);


}
