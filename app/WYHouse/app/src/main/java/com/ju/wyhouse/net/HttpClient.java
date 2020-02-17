package com.ju.wyhouse.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ju.wyhouse.entity.Constants;
import com.ju.wyhouse.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author:gaoju
 * Date:2020/1/18 13:51
 * Path:com.ju.wyhouse.net
 * Desc:okhttp主客户端
 */
public class HttpClient {

    private volatile static HttpClient mInstance;
    private OkHttpClient mOkHttpClient;
    private HttpResult mHttpResult;
    private OkHttpClient.Builder builder;

    //初始化OkHttpClient
    private HttpClient() {
        mHttpResult = new HttpResult();
        builder = new OkHttpClient.Builder();
        mOkHttpClient = builder
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .sslSocketFactory(NetUtil.createSSLSocketFactory())
                .hostnameVerifier(new NetUtil.TrustAllHostnameVerifier())
                .retryOnConnectionFailure(true)
                .build();

    }

    public static HttpClient getInstance() {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) {
                    mInstance = new HttpClient();
                }
            }
        }
        return mInstance;
    }


    /**
     * get和post请求，get1的RequestBody可为空
     *
     * @param url
     * @param requestCode
     * @param body
     */
    public void UrlToNet(String url, int requestCode, RequestBody body, final INetCallback callback) {
        //创建Request
        final Request request = HttpRequest.getInstance()
                .createRequestUrl(url, requestCode, body);

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //传递给处理结果类
                mHttpResult.startNetResult(Constants.REQUEST_FAILURE, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //传递给处理结果类
                mHttpResult.startNetResult(Constants.REQUEST_SUCCESS, response.body().string(), callback);
            }
        });

    }

    /**
     * 用Request请求
     *
     * @param request
     * @param callback
     */
    public void requestToNet(Request request, final INetCallback callback) {

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //传递给处理结果类
                mHttpResult.startNetResult(Constants.REQUEST_ERROR, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //传递给处理结果类
                String res = response.body().string();
                try {
                    int code = JSON.parseObject(res).getInteger("code");
                    LogUtil.i("response code：" + code);
                    checkResult(res, code, callback, request.url().toString());

                } catch (Exception e) {
                    mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "数据异常，请稍后再试 -null", callback);
                }
            }
        });
    }

    private void checkResult(String res, int code, INetCallback callback, String requestUrl) {
        LogUtil.i("request.url:" + requestUrl);
        String[] url = requestUrl.split("\\?");
        if (code == 0) {
            mHttpResult.startNetResult(Constants.REQUEST_SUCCESS, res, callback);
        } else if (code == -1) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "获取失败，请稍后再试 -1", callback);
        } else if (code == -2) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "系统错误，请联系管理员 -2", callback);
        } else if (code == -3) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "文件大小错误 -3", callback);
        } else if (code == -4) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "文件大小错误 -4", callback);
        } else if (code == -5) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "未绑定账号 -5", callback);
        } else if (code == -6) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "账号或密码错误 -6", callback);
        } else if (code == -7) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "接口未部署 -7", callback);
        } else if (code == -8) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "系统错误，请联系管理员 -8", callback);
        } else if (code == -9) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "请先完成评教 -9", callback);
        } else if (code == -10) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "身份验证失败 -10", callback);
        } else if (code == -11) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "信息不存在 -11", callback);
        } else if (code == -12) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "登陆失败，生成Token失败 -12", callback);
        } else if (code == -13) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "登陆失败，生成FlagId失败 -13", callback);
        } else if (code == -14) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "内容含有违法违规内容 -14", callback);
        } else if (code == -16) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "获取教务信息失败 -16", callback);
        } else if (code == -17) {
            if (url[0].equals(NetUrl.BASE_URL + NetUrl.REGISTER_URL)) {
                mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "教务处账号或者教务处密码错误 -17.1", callback);
            } else if (url[0].equals(NetUrl.BASE_URL + NetUrl.UPDATE_JW_URL)) {
                mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "教务处密码错误 -17.2", callback);
            } else {
                mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "教务处账号已过期，请到我的界面中的更新绑定进行绑定教务处账号 -17.2", callback);
            }

        } else if (code == -101) {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "登录失效，请重新等登录 -101", callback);
        } else {
            mHttpResult.startNetResult(Constants.REQUEST_FAILURE, "其它错误 -999", callback);
        }

    }


}
