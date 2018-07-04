package com.aillen.annhttp.net.http.anni;

import com.aillen.annhttp.net.contract.IHttpContracts;
import com.aillen.annhttp.net.http.HttpMethod;


import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by anlonglong on 2018/7/4.
 * Email： 940752944@qq.com
 */
public abstract class AnniAbstractRequest implements IHttpContracts.HttpRequest {

    private Map<String,String> mParams = new HashMap<>();
    private Map<String,String> mHeader = new HashMap<>();
    private MediaType mContentType = MediaType.parse("application/json; charset=utf-8");
    private HttpMethod mHttpMethod;
    private String mJsonStr;

    @Override
    public void addHeader(String key, String value) {
        mHeader.put(key,value);
    }

    @Override
    public void setContentType(MediaType contentType) {
           this.mContentType = contentType;
    }

    @Override
    public MediaType getContentType() {
        return mContentType;
    }

    @Override
    public HttpMethod getHttpMehtod() {
        return this.mHttpMethod;
    }

    @Override
    public void setParams(Map<String, String> params) {
        mParams.clear();
        this.mParams.putAll(params);
    }

    @Override
    public void putParams(String key, int value) {
        mParams.put(key,String.valueOf(value));
    }

    public void setJsonStr(String jsonStr) {
        mJsonStr = jsonStr;
    }

    public String getJsonStr() {
        return mJsonStr;
    }

    @Override
    public void putParams(String key, String value) {
        this.mParams.put(key,value);
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        mHttpMethod = httpMethod;
    }
}
