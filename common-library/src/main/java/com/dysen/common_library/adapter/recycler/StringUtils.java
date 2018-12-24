package com.dysen.common_library.adapter.recycler;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/19 - 上午10:38
 * @info
 */
public class StringUtils {
    /**
     * 获取非空的text
     */
    public static String obtainNoNullText(String content) {
        return obtainNoNullText(content, "");
    }

    /**
     * 获取非空的text，null或者empty时候可以设置默认值
     * 对content, defaultContent都进行判空操作
     */
    public static String obtainNoNullText(String content, @NonNull String defaultContent) {
        return !TextUtils.isEmpty(content) ? content
            : (!TextUtils.isEmpty(defaultContent) ? defaultContent : "");
    }
}
