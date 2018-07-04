package com.aillen.annhttp.net.http;


import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;


import com.aillen.annhttp.net.BaseHttpOutput;
import com.aillen.annhttp.net.LogUtil;
import com.aillen.annhttp.net.contract.IHttpContracts;
import com.aillen.annhttp.net.http.anni.AnniHttp;
import com.aillen.annhttp.net.http.anno.IRequest;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anlonglong on 2018/7/3.
 * Email： 940752944@qq.com
 * <p>
 * 因为入口是单利的所以这个类没必要写成单利的
 */
public class ExecutorRequest extends IExecutorRequest {

    private static final String TAG = "ExecutorRequest";
    private HttpListener mListener;
    private Context mContext;
    private DownloadListener mDownLoadListener;

    public ExecutorRequest(IRequest iRequest) {
        super(iRequest);
    }

    public ExecutorRequest(IHttpContracts.HttpResponse response) {
        super(response);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public <T extends BaseHttpOutput> Call executePostRequest(OkHttpClient okHttpClient, Request request, HttpListener<T> listener) {
        Call call = executeRequest(okHttpClient, request, listener);
        return call;
    }

    @Override
    public <T extends BaseHttpOutput> Call executeGetRequest(OkHttpClient okHttpClient, Request request, final HttpListener<T> listener) {
        Call call = executeRequest(okHttpClient, request, listener);
        return call;
    }

    private <T extends BaseHttpOutput> Call executeRequest(OkHttpClient okHttpClient, Request request, final HttpListener<T> listener) {
        checkNotNull(listener);
        this.mListener = listener;
        final AnniHttp.Result<T> tResult = new AnniHttp.Result<>();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailMessage(call, e, tResult);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LogUtil.i(TAG, "OriginalData = " + mMyResponse.getResponseOriginalData());
                    LogUtil.i(TAG, "ContentType() = " + mMyResponse.getContentType());
                    Class clazz = getActualType(listener);
                    T data = null;
                    //BaseHttpOutput data=null;
                    // d = (T) JSONObject.parseObject(getResponseOriginalData(), clazz);
                    //data = (BaseHttpOutput) gson.fromJson(getResponseOriginalData(), clazz);
                    try {
                        Gson gson = new Gson();
                        data = (T) gson.fromJson(mMyResponse.getResponseOriginalData(), clazz);
                    } catch (Exception e) {
                        LogUtil.e(TAG, e.getMessage());
                    }
                    tResult.setCall(call).setResponse(response).setData(data);
                    Message message = Message.obtain();
                    message.obj = tResult;
                    message.what = HttpListener.HttpState.HTTPSUCCESS.getCode();
                    if (!call.isCanceled()) {
                        mHandler.sendMessage(message);
                    }
                }
            }
        });
        return call;
    }

    @Override
    public Call downloadFileRequest(final String apkName, OkHttpClient okHttpClient, final okhttp3.Request request, final DownloadListener listener) {
        Call call = okHttpClient.newCall(request);
        this.mDownLoadListener = listener;
        final AnniHttp.Result<Pair<Long, Long>> tResult = new AnniHttp.Result<>();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailMessage(call, e, tResult);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String path = Environment.getExternalStorageDirectory().getPath() + "/updata";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    InputStream inputStream = null;
                    BufferedOutputStream outputStream = null;
                    int len = 0;
                    byte[] buf = new byte[1024];
                    long totalProgress = response.body().contentLength();
                    long currentProgress = 0;
                    long oldCurrentProgress = 0;
                    try {
                        inputStream = response.body().byteStream();
                        File apkFile = new File(file, apkName + ".apk");
                        if (apkFile.exists()) {
                            apkFile.delete();
                        }
                        apkFile.createNewFile();
                        outputStream = new BufferedOutputStream(new FileOutputStream(apkFile));

                        while ((len = inputStream.read(buf)) != -1) {
                            currentProgress += len;
                            outputStream.write(buf, 0, len);
                            if (currentProgress - oldCurrentProgress  >= 100000) {
                                System.out.println("oldCurrentProgress = [" + oldCurrentProgress + "], currentProgress = [" + currentProgress + "]");
                                Message message = mHandler.obtainMessage();
                                message.what = HttpListener.HttpState.UPDATA_SUCCESS.getCode();
                                Pair pair = new Pair(totalProgress, currentProgress);
                                tResult.setCall(call);
                                tResult.setData(pair);
                                message.obj = tResult;
                                mHandler.sendMessage(message);
                                oldCurrentProgress = currentProgress;
                            }
                            //listener.onResponse(call, totalProgress, currentProgress, (currentProgress * 1.0 / totalProgress) * 100);
                        }
                        outputStream.flush();
                    } catch (Exception e) {
                        System.out.println("Exception = " + e.getMessage());
                    }

                    System.out.println("-----------success --------------");
                }
            }
        });
        return call;
    }


    private <T> void sendFailMessage(Call call, IOException e, AnniHttp.Result<T> tResult) {
        System.out.println("-----------failire --------------");
        tResult.setCall(call).setException(e);
        Message message = Message.obtain();
        message.obj = tResult;
        message.what = HttpListener.HttpState.HTTPFAIL.getCode();
        mHandler.sendMessage(message);
    }

    private <T extends BaseHttpOutput> Class getActualType(HttpListener<T> listener) {
        /**
         *传进来的泛型实际的类型必须要有父类(随便自定义一个继承就可以了,该父类可以什么都不写),
         * 因为通过下面的方法获取泛型实际类型的时候,首先要获取父类的类型,如无法找到, 返回Object.class
         * //通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class
         * **/
        Class data = null;
        Type[] types = listener.getClass().getGenericInterfaces();
        if (types != null && types.length > 0) {
            Type[] params = ((ParameterizedType) types[0]).getActualTypeArguments();
            if (params != null && params.length > 0) {
                data = (Class) params[0];
            }
        }
        return data;
    }

    private Object checkNotNull(Object o) {
        if (null == o) {
            throw new NullPointerException(o.toString() + " is null ");
        }
        return o;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            AnniHttp.Result result = (AnniHttp.Result) msg.obj;
            switch (HttpListener.HttpState.get(msg.what)) {
                case HTTPSUCCESS:
                    try {
                        if (null != mListener) {
                            mListener.onResponse(result.getCall(), result.getResponse(), (BaseHttpOutput) result.getData());
                        }
                    } catch (Exception e) {
                        LogUtil.i(TAG, e.getMessage());
                    }
                    break;
                case HTTPFAIL:
                    if (null != mListener) {
                        mListener.onFailure(result.getCall(), result.getException());
                    }
                    if (null != mDownLoadListener) {
                        mDownLoadListener.onFailure(result.getCall(), result.getException());
                    }
                    break;
                case UPDATA_SUCCESS:
                    if (null != mDownLoadListener) {
                        Pair<Long, Long> data = (Pair<Long, Long>) result.getData();
                        BigDecimal bigDecimal = new BigDecimal(data.second * 1.0 / data.first).setScale(2, RoundingMode.UP);
                        mDownLoadListener.onResponse(result.getCall(), data.first, data.second, bigDecimal.doubleValue());
                    }
                    break;
                default:

            }

        }
    };


}
