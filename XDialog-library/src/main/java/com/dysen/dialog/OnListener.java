package com.dysen.dialog;

import android.view.View;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/ToastUtils
 * time   : 2019/01/04
 * desc   : View 的点击事件封装
 */
public class OnListener {

    public interface OnClickListener<V extends View> {
        void onClick(XDialog toast, V view);
    }

    public interface OnLongClickListener<V extends View> {
        void onLongClick(XDialog toast, V view);
    }
}