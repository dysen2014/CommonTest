package com.dysen.common_library.utils;

import android.util.Log;

/**
 * @package com.vip.zb.tool
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/21 - 下午2:34
 * @info Log的辅助类
 */
public class LogUtils {

    public static final String TAG = "dysen";
    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static boolean isDebug = true;
//    public static boolean isDebug = AppContext.getInstance().getDeBugMode();

    public LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 下面五个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            LogUtils.d(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.i(tag, msg, tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.i(tag, msg, tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.i(tag, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug)
            Log.i(tag, msg, tr);
    }
}

