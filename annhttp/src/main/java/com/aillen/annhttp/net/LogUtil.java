package com.aillen.annhttp.net;


import android.util.Log;

/**
 * 作者：anlonglong on 2017/10/2 12:31
 */

public class LogUtil {
    private LogUtil() {
        throw new IllegalStateException("you can't instance me 抽!!(￣ε(#￣)☆╰╮(￣▽￣///)");
    }
    public static boolean enable =  true;

    public static void i(String tag,String msg) {
        if (enable) {
            Log.i(tag,msg);
        }
    }

    public static void e(String tag,String msg, Throwable throwable) {
        if (enable) {
            Log.e(tag, msg,throwable);
        }
    }

    public static void e(String tag,String msg) {
        if (enable) {
            Log.e(tag, msg,null);
        }
    }

    public static void d(String tag,String msg, Throwable th) {
        if (enable) {
            Log.d(tag,msg,th);
        }
    }

    public static void d(String tag,String msg) {
        if (enable) {
            Log.d(tag,msg,null);
        }
    }

    public static void v(String tag,String msg,Throwable th) {
        if (enable) {
            Log.v(tag,msg,th);
        }
    }

    public static void v(String tag,String msg) {
        if (enable) {
            Log.v(tag,msg,null);
        }
    }

    public static void w(String tag,String mag,Throwable th) {
        if (enable) {
            Log.w(tag,mag,th);
        }
    }

    public static void w(String tag,String mag) {
        if (enable) {
            Log.w(tag,mag,null);
        }
    }



}
