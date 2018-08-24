package com.dysen.common_library.subscribers;

import com.dysen.common_library.http.HttpResult;

/**
 * xiezuofei
 * 2018-01-23 14:20
 * 793169940@qq.com
 */
public interface SubscriberNextOrErrorListener<T> {
    void onNext(HttpResult<T> t);
    void onError(String error);
}
