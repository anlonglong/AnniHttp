package com.aillen.annhttp.net.http.anni;

import com.aillen.annhttp.net.LogUtil;
import com.aillen.annhttp.net.contract.IHttpContracts;
import com.aillen.annhttp.net.http.anno.IRequest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by：anlonglong
 * on 2017/10/23 16:35
 * Email: anlonglong0721@gmail.com
 */

public class AnniInterceptor implements Interceptor {


    public static final String TAG = AnniInterceptor.class.getSimpleName();
    private IHttpContracts.HttpResponse mResponse;

    private IRequest mRequest;

    public AnniInterceptor(IRequest request) {
        this.mRequest = request;
    }

    public AnniInterceptor(IHttpContracts.HttpResponse response) {
        this.mResponse = response;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtil.i(TAG, "method = " + request.method());
        LogUtil.i(TAG, "url = " + request.url().toString());
        Response proceed = chain.proceed(request);
        if (!proceed.request().url().toString().contains(".apk")) {
            String string = proceed.body().string();
            MediaType contentType = proceed.body().contentType();
            if (null != mRequest) {
                mRequest.setReponseOriginalData(string);
                mRequest.setContentType(contentType);
            }
            if (null != mResponse) {
                mResponse.setResponseOriginalData(string);
                mResponse.setContentType(contentType);
            }

        }
        return proceed;
    }
}
