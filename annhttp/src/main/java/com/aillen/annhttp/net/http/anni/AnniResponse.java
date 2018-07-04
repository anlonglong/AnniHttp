package com.aillen.annhttp.net.http.anni;

import com.aillen.annhttp.net.contract.IHttpContracts;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by anlonglong on 2018/7/4.
 * Email： 940752944@qq.com
 */
public class AnniResponse implements IHttpContracts.HttpResponse {

    private String mData;
    private MediaType mContentType;
    private Map<String,String> mHeader = new HashMap<>();

    @Override
    public String getResponseOriginalData() {
        return mData;
    }

    @Override
    public void setResponseOriginalData(String data) {
        this.mData = data;
    }

    @Override
    public MediaType getContentType() {
        return mContentType;
    }


    @Override
    public void setContentType(MediaType contentType) {
        this.mContentType = contentType;
    }

    public void setHeader(Map<String, String> header) {
        mHeader = header;
    }

    @Override
    public Map<String, String> getHeader() {
        return mHeader;
    }
}
