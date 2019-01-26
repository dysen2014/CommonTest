package com.dysen.dialog;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/ToastUtils
 *    time   : 2019/01/04
 *    desc   : Toast 显示销毁监听
 */
public interface OnToastLifecycle {

    /**
     * 显示监听
     */
    void onShow(XDialog toast);

    /**
     * 消失监听
     */
    void onDismiss(XDialog toast);
}