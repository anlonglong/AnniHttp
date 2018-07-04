package com.aillen.annhttp.net.http;

import com.aillen.annhttp.net.BaseHttpOutput;
import com.aillen.annhttp.net.LogUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by：anlonglong
 * on 2017/10/23 15:44
 * Email: anlonglong0721@gmail.com
 */

public interface HttpListener<T extends BaseHttpOutput> {

    enum HttpState {
        HTTPSUCCESS(0x01),
        HTTPFAIL(0x02),
        UPDATA_SUCCESS(0x03);

        private int code;

        HttpState(int i) {
            this.code = i;
        }

        public int getCode() {
            return code;
        }

        public static HttpState state(String ste) {
            HttpState state = null;
            try {
                state = valueOf(ste);
            } catch (Exception e) {
                LogUtil.e("Unexpected HttpState", e.getMessage());
            }
            return state;
        }

        public static HttpState get(int ste)  {
           if (ste == HTTPSUCCESS.code) return HTTPSUCCESS;
           if (ste == HTTPFAIL.code) return HTTPFAIL;
           if (ste == UPDATA_SUCCESS.code) return UPDATA_SUCCESS;
             return null;
        }

    }

     void onFailure(Call call, IOException e);
     void onResponse(Call call, Response response, T data) throws IOException;
}
