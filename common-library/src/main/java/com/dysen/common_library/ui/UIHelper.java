package com.dysen.common_library.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

    public final static String TAG = "UIHelper";

    public final static int RESULT_OK = 0x00;
    public final static int REQUEST_CODE = 0x01;
    private static Toast toast;

    public static void ToastMessage(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (toast == null)
            toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.show();
    }

    public static void ToastMessage(Context cont, int msg) {
        if (cont == null || msg <= 0) {
            return;
        }
        if (toast == null)
            toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        if (cont == null || msg == null) {
            return;
        }
        if (toast == null)
            toast = Toast.makeText(cont, msg, time);
        else
            toast.setText(msg);
        toast.show();
    }

    /**
     * Toast 显示自定义View
     * @param context
     * @param view
     */
    public static Toast ToastView(Context context, View view) {

        if (toast == null)
            /*创建新Toast对象*/
            toast = new Toast(context);

        toast.setView(view);
        /*设置Toast显示时间*/
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        /*显示Toast*/
//        toast.show();
        return toast;
    }

    /**
     *      * Toast 显示自定义View
     * @param context
     * @param view
     * @param duration 显示时长
     */
    public static void ToastView(Context context, View view, int duration) {

        showMyToast(ToastView(context, view), duration);
    }

    public static void showMyToast(final Toast toast, final int duration) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 2000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, duration);
    }

    public static void showNext(Activity context, Class cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showNext(Activity context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }


    public static void showNext(Activity context, Class cls, String type) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        context.startActivity(intent);
    }
}
