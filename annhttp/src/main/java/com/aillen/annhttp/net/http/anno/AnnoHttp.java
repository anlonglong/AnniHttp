package com.aillen.annhttp.net.http.anno;


import com.aillen.annhttp.net.BaseHttpOutput;
import com.aillen.annhttp.net.http.DownloadListener;
import com.aillen.annhttp.net.http.ExecutorRequest;
import com.aillen.annhttp.net.http.HttpListener;
import com.aillen.annhttp.net.http.HttpMethod;
import com.aillen.annhttp.net.http.IExecutorRequest;

import okhttp3.Call;

/**
 * Created by anlonglong on 2018/7/4.
 * Email： 940752944@qq.com
 */
public class AnnoHttp {
    private AnnoRequestWrapper mWrapper;
    private static final Object mlock = new Object();
    private static AnnoHttp mRequest;
    private Call mCall;
    private IExecutorRequest mExecutorRequest;

    private AnnoHttp() {

    }

    public void setExecutorRequest(IExecutorRequest executorRequest) {
        mExecutorRequest = executorRequest;
    }

    public AnnoHttp setRequestWrapper(AnnoRequestWrapper wrapper) {
        mWrapper = wrapper;
        mExecutorRequest = new ExecutorRequest(mWrapper);
        mWrapper.addHeader(mWrapper.getRequest());
        return this;
    }

    public static AnnoHttp getAnnoHttp() {
        if (null == mRequest) {
            synchronized (mlock) {
                if (null == mRequest) {
                    mRequest = new AnnoHttp();
                }
            }
        }
        return mRequest;
    }


    /**
     *
     * @param method
     * @param listener
     * @param <T> 返回值的类型
     * @return  返回值可用于取消当前的请求.
     */
    public <T extends BaseHttpOutput> Call executeRequest(HttpMethod method, HttpListener<T> listener) {
        if (mWrapper == null) {
            throw new NullPointerException("请求前需要调 setRequestWrapper() 方法,设置请求的参数 ");
        }
        if (HttpMethod.GET == method) {
            this.mCall = mExecutorRequest.executeGetRequest(mWrapper.getOkHttpClient(),mWrapper.getRequest(),listener);
        } else if (HttpMethod.POST == method) {
            this.mCall = mExecutorRequest.executePostRequest(mWrapper.getOkHttpClient(),mWrapper.getRequest(),listener);
        }
        return mCall;
    }

    public Call executeDownloadRequest(String apkName, DownloadListener listener) {
        if (mWrapper == null) {
            throw new NullPointerException("请求前需要调 setRequestWrapper() 方法,设置请求的参数 ");
        }
        this.mCall = mExecutorRequest.downloadFileRequest(apkName,mWrapper.getOkHttpClient(),mWrapper.getRequest(),listener);
        return mCall;
    }
}
