package com.aillen.annhttp.net.http;

import com.aillen.annhttp.net.BaseHttpOutput;
import com.aillen.annhttp.net.contract.IHttpContracts;
import com.aillen.annhttp.net.http.anno.IRequest;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by anlonglong on 2018/7/3.
 * Email： 940752944@qq.com
 */
public abstract class IExecutorRequest {
    protected IRequest mRequest;

    protected static IHttpContracts.HttpResponse mMyResponse;

    public IExecutorRequest(IRequest iRequest) {
        this.mRequest = iRequest;
    }

    public IExecutorRequest(IHttpContracts.HttpResponse myRequest) {
        this.mMyResponse = myRequest;
    }

    public  abstract <T extends BaseHttpOutput> Call executeGetRequest(OkHttpClient okHttpClient, okhttp3.Request request, final HttpListener<T> listener);

    public abstract  <T extends BaseHttpOutput> Call executePostRequest(OkHttpClient okHttpClient, okhttp3.Request request, HttpListener<T> listener);


    public abstract Call downloadFileRequest(final String apkName, OkHttpClient okHttpClient, okhttp3.Request request, final DownloadListener listener);

}
