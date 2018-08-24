package com.dysen.common_library.subscribers;

import com.dysen.common_library.http.HttpResult;

/**
 * xiezuofei
 * 2017-12-01 10:20
 * 793169940@qq.com
 *
 */
public interface SubscriberOnNextListener<T> {
    void onNext(HttpResult<T> t);
}
