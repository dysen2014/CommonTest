package com.dysen.image;

import android.app.Application;
import android.os.StrictMode;

/**
 * @package com.dysen.image
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/20 - 下午3:40
 * @info
 */
public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
