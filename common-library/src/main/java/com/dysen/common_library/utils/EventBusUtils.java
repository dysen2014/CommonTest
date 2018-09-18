package com.dysen.common_library.utils;

import android.os.Message;

/**
 * @package com.dysen.common_library.utils
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/1 - 下午6:02
 * @info
 */
public class EventBusUtils {

    public static int TEST_MSG = 1001;

    public static void test_msg(String test) {
        Message message = new Message();
        message.what = TEST_MSG;
        message.obj = test;
        Tools.eventBusPost(message);
    }

    public static boolean isTest_msg(Message message) {
        return message != null && message.what == TEST_MSG;
    }
}
