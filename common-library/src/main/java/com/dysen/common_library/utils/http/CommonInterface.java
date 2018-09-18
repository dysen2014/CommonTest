package com.dysen.common_library.utils.http;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dysen on 2018/4/17.
 * 自定义接口类
 */
public class CommonInterface {

    public interface OnItemClick<T> {
        // 点击事件
        void onClick(View view, T t);
        // 长按事件
//		void onLongClick(View view, T t);
    }

    public interface OnItemClicks<T> {
        // 点击事件
        void onClick(View view, T t);

        // 长按事件
        void onLongClick(View view, T t);
    }

    public interface NetworkResponse {

        //请求返回成功
        void onSuccess(int statusCode, String response) throws JSONException;
        //请求返回失败
        void onFailure(Throwable e, String errorResponse);
    }

    public abstract class NetworkResponseHandler {

        public void onSuccess(int statusCode, JSONObject response) {

        }

        public void onSuccess(int statusCode, String response) {

        }

        public void onSuccess(int statusCode, byte[] response) {

        }

        public void onFailure(Throwable e, JSONObject errorResponse) {

        }

        public void onFailure(Throwable e, String errorResponse) {

        }
    }

    public interface HandleBackInterface {
        boolean onBackPressed();
    }
}


