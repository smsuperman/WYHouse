package com.ju.wyhouse.net;

import com.ju.wyhouse.entity.Constants;

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
                mHttpResult.startNetResult(Constants.REQUEST_ERROR, e.getMessage(), callback);
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
                mHttpResult.startNetResult(Constants.REQUEST_SUCCESS, response.body().string(), callback);
            }
        });
    }


}
