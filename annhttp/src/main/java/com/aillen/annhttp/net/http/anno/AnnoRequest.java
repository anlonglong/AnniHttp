package com.aillen.annhttp.net.http.anno;

import com.aillen.annhttp.net.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by：anlonglong
 * on 2017/10/23 10:38
 * Email: anlonglong0721@gmail.com
 */

public abstract class AnnoRequest implements IRequest {


    private Map<String,String> mParams = new HashMap<>();
    private Map<String,String> mHeader = new HashMap<>();
    private MediaType mContentType;
    private String mData;
    private HttpMethod mHttpMethod;
    private String mJsonStr;


    public AnnoRequest() {

    }

    @Override
    public void addHeader(String key, String value) {
        mHeader.put(key,value);
    }

    @Override
    public Map<String, String> getHeader() {
        return mHeader;
    }

    @Override
    public void setReponseOriginalData(String data) {
        mData = data;
    }


    @Override
    public MediaType getContentType() {
        return mContentType;
    }

    @Override
    public String getReponseOriginalData() {
        return mData;
    }

    @Override
    public void setContentType(MediaType contentType) {
        this.mContentType = contentType;
    }

    @Override
    public void setParams(Map<String, String> params) {
        mParams.clear();
        this.mParams.putAll(params);
    }

    public String getJsonStr() {
        return mJsonStr;
    }


    public void setJsonStr(String jsonStr) {
        mJsonStr = jsonStr;
    }

    @Override
    public void putParams(String key, int value) {
        mParams.put(key,String.valueOf(value));
    }

    @Override
    public void putParams(String key, String value) {
        mParams.put(key,value);
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        mHttpMethod = httpMethod;
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public String getRequestParamsWithUrl() {
        return mData;
    }

    public void cleanParams() {
        mParams.clear();
    }

}
