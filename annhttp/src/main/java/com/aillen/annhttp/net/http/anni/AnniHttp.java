package com.aillen.annhttp.net.http.anni;

import com.aillen.annhttp.net.BaseHttpOutput;
import com.aillen.annhttp.net.contract.IHttpContracts;
import com.aillen.annhttp.net.http.DownloadListener;
import com.aillen.annhttp.net.http.ExecutorRequest;
import com.aillen.annhttp.net.http.HttpListener;
import com.aillen.annhttp.net.http.HttpMethod;
import com.aillen.annhttp.net.http.IExecutorRequest;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by：anlonglong
 * on 2017/10/23 12:03
 * Email: anlonglong0721@gmail.com
 */

public class AnniHttp {

    private static final Object mlock = new Object();
    private static AnniHttp sAnniHttp;
    private Call mCall;
    private IExecutorRequest mExecutorRequest;
    private IHttpContracts.HttpRequest mRequest;

    private AnniHttp() {

    }

    public static void init(){

    }

    public AnniHttp setExecutorRequest(IExecutorRequest executorRequest) {
        this.mExecutorRequest = executorRequest;
        return this;
    }


    public AnniHttp setAnniRequest(IHttpContracts.HttpRequest myRequest) {
        this.mRequest = myRequest;
        AnniResponse response = new AnniResponse();
        myRequest.addInterceptor(new AnniInterceptor(response));
        mExecutorRequest = new ExecutorRequest(response);
        return this;
    }

    public static AnniHttp getAnniHttp() {
        if (null == sAnniHttp) {
            synchronized (mlock) {
                if (null == sAnniHttp) {
                    sAnniHttp = new AnniHttp();
                }
            }
        }
        return sAnniHttp;
    }

    public <T extends BaseHttpOutput> Call executeHttpRequest(HttpMethod method, HttpListener<T> listener) {
        if (mRequest == null) {
            throw new NullPointerException("请求前需要调 setAnnoRequestWrapper() 方法,设置请求的参数 ");
        }
        if (HttpMethod.GET == method) {
            this.mCall = mExecutorRequest.executeGetRequest(mRequest.getOkHttpClient(), mRequest.getRequest(),listener);
        } else if (HttpMethod.POST == method) {
            this.mCall = mExecutorRequest.executePostRequest(mRequest.getOkHttpClient(), mRequest.getRequest(),listener);
        }
        return mCall;
    }
    /**
     *
     * @param method
     * @param listener
     * @param <T> 返回值的类型
     * @return  返回值可用于取消当前的请求.
     */
    public <T extends BaseHttpOutput> Call executeRequest(HttpMethod method, HttpListener<T> listener) {
        if (mRequest == null) {
            throw new NullPointerException("请求前需要调 setAnnoRequestWrapper() 方法,设置请求的参数 ");
        }
        if (HttpMethod.GET == method) {
            this.mCall = mExecutorRequest.executeGetRequest(mRequest.getOkHttpClient(),mRequest.getRequest(),listener);
        } else if (HttpMethod.POST == method) {
            this.mCall = mExecutorRequest.executePostRequest(mRequest.getOkHttpClient(),mRequest.getRequest(),listener);
        }
        return mCall;
    }

    public Call executeDownloadRequest(String apkName, DownloadListener listener) {
        if (mRequest == null) {
            throw new NullPointerException("请求前需要调 setAnnoRequestWrapper() 方法,设置请求的参数 ");
        }
        this.mCall = mExecutorRequest.downloadFileRequest(apkName,mRequest.getOkHttpClient(),mRequest.getRequest(),listener);
        return mCall;
    }

    public Call getCall() {
        return mCall;
    }

    public void cancelRequest() {
        if (null != mCall) {
            mCall.cancel();
        }
    }

    /**
     * http 返回的数据封装类
     * 因为okhttp的请求回调在子线程里,
     * 我们只能通过handler发送到主线程,
     * 而handler只能携带一个参数,而回调的参数有还几个,
     * 所以我们把数据封装好,统一发送.
     *
     * @param <T>
     */
    public static class Result<T> {
        private Call call;
        private IOException exception;
        private T data;
        private Response response;

        public Result() {
        }

        public Result(Call call, IOException e, T data, Response response) {
            this.call = call;
            this.exception = e;
            this.data = data;
            this.response = response;
        }

        public Call getCall() {
            return call;
        }

        public Result setCall(Call call) {
            this.call = call;
            return this;
        }


        public IOException getException() {
            return exception;
        }

        public Result setException(IOException exception) {
            this.exception = exception;
            return this;
        }

        public T getData() {
            return data;
        }

        public Result setData(T data) {
            this.data = data;
            return this;
        }

        public Response getResponse() {
            return response;
        }

        public Result setResponse(Response response) {
            this.response = response;
            return this;
        }
    }
}
