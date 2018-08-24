package com.dysen.common_library.subscribers;

import com.dysen.common_library.http.ResMsg;

/**
 * xiezuofei
 * 2018-01-23 14:20
 * 793169940@qq.com
 */
public interface SubscriberNextOrErrorListener3 {
    void onNext(ResMsg resMsg);
    void onError(String error);
}
