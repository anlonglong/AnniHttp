package com.aillen.annhttp.net.http;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by anlonglong on 2018/7/3.
 * Email： 940752944@qq.com
 */
public interface DownloadListener {

    void onResponse(Call call, long totalProgress, long currentProgress, double currentPercent);

    void onFailure(Call call, IOException e);
}
