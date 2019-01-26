package com.dysen.dialog;

import android.view.View;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/ToastUtils
 * time   : 2019/01/04
 * desc   : {@link View.OnClickListener} 包装类
 */
final class ViewClickHandler implements View.OnClickListener, View.OnLongClickListener {

    private final XDialog mToast;
    private OnListener.OnClickListener mListener;
    private OnListener.OnLongClickListener mLongListener;

    ViewClickHandler(XDialog toast, View view, OnListener.OnClickListener listener) {
        mToast = toast;
        mListener = listener;

        view.setOnClickListener(this);
    }

    ViewClickHandler(XDialog toast, View view, OnListener.OnLongClickListener listener) {
        mToast = toast;
        mLongListener = listener;

        view.setOnLongClickListener(this);
    }

    @Override
    public final void onClick(View v) {
        mListener.onClick(mToast, v);
    }

    @Override
    public boolean onLongClick(View v) {
        mLongListener.onLongClick(mToast, v);
        return true;//true 拦截点击事件
    }
}