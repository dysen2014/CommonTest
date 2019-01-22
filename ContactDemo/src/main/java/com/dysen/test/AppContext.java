package com.dysen.test;

import com.dysen.common_library.base.BaseAppContext;
import com.zhy.changeskin.SkinManager;

/**
 * @package com.dysen.test
 * @email dy.sen@qq.com
 * created by dysen on 2019/1/22 - 10:12 AM
 * @info
 */
public class AppContext extends BaseAppContext {
    private static AppContext app;

    public static AppContext getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SkinManager.getInstance().init(this);
    }
}
