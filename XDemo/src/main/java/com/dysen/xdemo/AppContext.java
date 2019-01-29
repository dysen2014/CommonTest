package com.dysen.xdemo;

import com.dysen.common_library.base.BaseAppContext;
import com.dysen.toast.ToastUtils;
import com.dysen.toast.style.ToastBlackStyle;

/**
 * @package com.dysen.xdemo
 * @email dy.sen@qq.com
 * created by dysen on 2019/1/25 - 11:40 AM
 * @info
 */
public class AppContext extends BaseAppContext {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this, new ToastBlackStyle());
    }
}
