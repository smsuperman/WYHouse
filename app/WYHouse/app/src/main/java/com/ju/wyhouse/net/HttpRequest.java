package com.ju.wyhouse.net;

import android.text.TextUtils;

import com.ju.wyhouse.entity.Constants;
import com.ju.wyhouse.utils.LogUtil;

import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author:gaoju
 * Date:2020/1/18 13:53
 * Path:com.ju.wyhouse.net
 * Desc: Okhttp的Request封装
 */
public class HttpRequest {

    private static volatile HttpRequest mInstance;


    private HttpRequest() {
    }


    public static HttpRequest getInstance() {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) {
                    mInstance = new HttpRequest();
                }
            }
        }
        return mInstance;
    }

    /**
     * 用Url创建Request
     *
     * @param url
     * @param requestCode
     * @param requestBody
     * @return
     */
    public Request createRequestUrl(String url, int requestCode, RequestBody requestBody) {
        if (requestCode == Constants.REQUEST_GET) {
            return new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        } else if (requestCode == Constants.REQUEST_POST) {
            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        return null;

    }

    /**
     * 自定义Request
     *
     * @param url
     * @param queryMap
     * @return
     */
    public Request createRequestParams(String url, int requestCode, RequestBody requestBody, Map<String, String> queryMap) {
        Request.Builder reqBuild = new Request.Builder();
        //添加请求头
        if (queryMap != null) {
            if (!TextUtils.isEmpty(queryMap.get("Wsw-Flag"))) {
                LogUtil.i("flag:" + queryMap.get("Wsw-Flag"));
                reqBuild.addHeader("Wsw-Flag", queryMap.get("Wsw-Flag"));
            }
        }


        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder();

        //遍历请求参数
        if (queryMap != null) {
            Set<Map.Entry<String, String>> set = queryMap.entrySet();
            if (set.size() > 0) {
                for (Map.Entry<String, String> entry : set) {
                    if (entry.getKey().equals("Wsw-Flag")) {
                        continue;
                    }
                    if (TextUtils.isEmpty(entry.getValue())) {
                        continue;
                    }
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        if (requestCode == Constants.REQUEST_GET) {
            return reqBuild.url(urlBuilder.build())
                    .get()
                    .build();
        } else if (requestCode == Constants.REQUEST_POST) {
            return reqBuild.url(urlBuilder.build())
                    .post(requestBody)
                    .build();
        }

        return null;
    }
}
