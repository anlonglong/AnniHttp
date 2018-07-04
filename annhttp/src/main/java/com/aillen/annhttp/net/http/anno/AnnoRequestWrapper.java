package com.aillen.annhttp.net.http.anno;

import android.content.Context;

import com.aillen.annhttp.BuildConfig;
import com.aillen.annhttp.net.LogUtil;
import com.aillen.annhttp.net.http.Constant;
import com.aillen.annhttp.net.http.ExecutorRequest;
import com.aillen.annhttp.net.http.HttpMethod;
import com.aillen.annhttp.net.http.NetWorkConfiguration;
import com.aillen.annhttp.net.http.anni.AnniInterceptor;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by：anlonglong
 * on 2017/10/23 11:05
 * Email: anlonglong0721@gmail.com
 */

public class AnnoRequestWrapper extends AnnoRequest {

    private static final String TAG = "AnnoRequestWrapper";

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static WeakReference<Context> mReferenceCtx;//cache用的
    /**
     * 用于创建OkHttpClient
     */
    private OkHttpClient.Builder mBuilder;
    /**
     * 请求参数的封装
     */
    private okhttp3.Request.Builder mRequestBuilder;
    /**
     * 网络请求的有一些配置
     * eg:超时时间,是否需要缓存等.
     */
    private NetWorkConfiguration mConfiguration;

    private ExecutorRequest mExecutorRequest;

    public static void initContext(Context context) {
        mReferenceCtx = new WeakReference<>(context);
    }


    public AnnoRequestWrapper() {
        mConfiguration = new NetWorkConfiguration(mReferenceCtx.get());
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(mConfiguration.getConnectTimeOut(), TimeUnit.MILLISECONDS)
                .connectionPool(mConfiguration.getConnectionPool())
                .retryOnConnectionFailure(true);
        if (mConfiguration.getIsCache()) {
            mBuilder.cache(mConfiguration.getDiskCache());
        }

        if (BuildConfig.DEBUG) {
            mBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        }
        setConfiguration(mConfiguration);
        mRequestBuilder = new okhttp3.Request.Builder();
        addInterceptor(new AnniInterceptor(this));
    }


    public void setConfiguration(NetWorkConfiguration configuration) {
        mConfiguration = configuration;
    }

    public void addInterceptor(Interceptor interceptor) {
        if (null != interceptor) {
            mBuilder.addInterceptor(interceptor);
        }

    }

    public void addNetworkInterceptor(Interceptor interceptor) {
        if (null != interceptor) {
            mBuilder.addNetworkInterceptor(interceptor);
        }

    }

    public okhttp3.Request getRequest() {
        if (mConfiguration.getIsCache()) {
            mRequestBuilder.cacheControl(CacheControl.FORCE_CACHE);
        }
        okhttp3.Request build = mRequestBuilder.build();
        return build;
    }

    /**
     * json格式额post请求
     *
     * @param jsonObj
     */
    public void setPostBodyJson(Object jsonObj) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(jsonObj);
        setJsonStr(jsonStr);
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        mRequestBuilder.post(requestBody);
    }


    public void setDownloadUrl(String fileUrl) {
        setUrl(fileUrl);
    }

    /**
     * map格式额post请求
     *
     * @param postBodyMap
     */
    public void setPostBodyMap(Map<String, String> postBodyMap) {
        checkNotNull(postBodyMap);
        if (postBodyMap.isEmpty()) return;
        Set<Map.Entry<String, String>> entries = postBodyMap.entrySet();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        mRequestBuilder.post(builder.build());
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient client = mBuilder.build();
        return client;
    }

    public void addHeader() {
        if (!getHeader().isEmpty()) {
            Headers headers = Headers.of(getHeader());
            mRequestBuilder.headers(headers);
        }
    }

    @Override
    public String getBaseUrl() {
        return Constant.BASE_URL;
    }

    @Override
    public void setUrl(String url) {
        Constant.BASE_URL = url;
        try {
            mRequestBuilder.url(url + getRequestParamsWithUrl());
        } catch (NullPointerException e) {
            LogUtil.i(TAG, " url 不能为空!!!");
        }
    }

    private Object checkNotNull(Object o) {
        if (null == o) {
            throw new NullPointerException(o.toString() + " is null ");
        }
        return o;
    }

    public void addHeader(okhttp3.Request request) {
        if (!getHeader().isEmpty()) {
            Map<String, String> header = getHeader();
            Set<String> keySet = header.keySet();
            okhttp3.Request.Builder builder = request.newBuilder();
            for (String key : keySet) {
                builder.addHeader(key, header.get(key));
            }
        }
    }


    @Override
    public HttpMethod getHttpMehtod() {
        return HttpMethod.Method(mRequestBuilder.build().method());
    }

    @Override
    public String getRequestParamsWithUrl() {
        StringBuffer sb = new StringBuffer();
        if (!getParams().isEmpty()) {
            sb.append('?');
            Set<Map.Entry<String, String>> entries = getParams().entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            int pos = 0;
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                ++pos;
                sb.append(String.format("%s=%s", next.getKey(), next.getValue()));
                if (pos < entries.size()) {
                    sb.append('&');
                }
            }
        }
        return sb.toString();
    }

}
