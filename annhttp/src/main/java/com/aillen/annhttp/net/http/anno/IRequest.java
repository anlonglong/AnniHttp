package com.aillen.annhttp.net.http.anno;

import com.aillen.annhttp.net.http.HttpMethod;


import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by：anlonglong
 * on 2017/10/23 10:21
 * Email: anlonglong0721@gmail.com
 */

public interface IRequest {


    void addHeader(String key, String value);

    Map<String, String> getHeader();

    void setContentType(MediaType contentType);

    MediaType getContentType();

    void setUrl(String url);

    String getBaseUrl();

    HttpMethod getHttpMehtod();

    void setParams(Map<String, String> params);

    void putParams(String key, int value);

    void putParams(String key, String value);

    Map<String, String> getParams();

    String getRequestParamsWithUrl();

    String getReponseOriginalData();

    void setReponseOriginalData(String data);


}
