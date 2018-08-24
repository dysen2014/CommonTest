package com.dysen.common_library.subscribers;


/**
 * xiezuofei
 * 2018-01-23 14:20
 * 793169940@qq.com
 */
public interface SubscriberNextOrErrorListener2<T> {
    void onNext(T t);
    void onError(String error);
}
