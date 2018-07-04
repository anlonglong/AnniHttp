package com.aillen.annhttp.net;


import com.aillen.annhttp.net.contract.IHttpContracts;

import okhttp3.Interceptor;

/**
 * Created by anlonglong on 2018/7/4.
 * Email： 940752944@qq.com
 */
public class Anni {

    private static IHttpContracts.HttpRequest sRequest;
    private static IHttpContracts.HttpResponse sReponse;

    private Anni(){}

    public static void init(IHttpContracts.HttpRequest request, IHttpContracts.HttpResponse response, Interceptor interceptor){
        sRequest.addInterceptor(interceptor);
    }
}
