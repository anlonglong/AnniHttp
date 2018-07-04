package com.aillen.annhttp.net.contract;/**
 * Created by anlonglong on 2017/12/20.
 */


import com.aillen.annhttp.net.http.HttpListener;
import com.aillen.annhttp.net.http.HttpMethod;

import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by anlonglong
 * <p>
 * on 2017/12/20
 * <p>
 * desc: http请求和响应的一些信息
 * <p>
 * Email : anlonglong0721@gamil.com
 */
public interface IHttpContracts {
    public interface HttpRequest {

        void addHeader(String key, String value);

        public void addInterceptor(Interceptor interceptor);

        void setContentType(MediaType contentType);

        MediaType getContentType();

        String getRequestParamsWithUrl();

        void setUrl(String url);

        String getBaseUrl();

        HttpMethod getHttpMehtod();

        void setParams(Map<String, String> params);

        void putParams(String key, int value);

        void putParams(String key, String value);

        Map<String,String> getParams();

        public OkHttpClient getOkHttpClient();

        public okhttp3.Request getRequest();

    }

    public interface HttpResponse {

        String getResponseOriginalData();

        void setResponseOriginalData(String data);

        MediaType getContentType();

        void  setHeader(Map<String, String> header);

        void setContentType(MediaType contentType);

        Map<String, String> getHeader();

    }

    void doRequest(HttpMethod method, Request request, HttpListener listener);
}
