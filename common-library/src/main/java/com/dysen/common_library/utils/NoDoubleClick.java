package com.dysen.common_library.utils;

/**
 * @package com.dysen.common_library.utils
 * @email dy.sen@qq.com
 * created by dysen on 2019/4/30 - 18:02
 * @info
 */
public class NoDoubleClick {
    public static final int INTERVALS_TIME_300 = 300;

    private long lastClickTime = 0;
    private int intervalsTime = INTERVALS_TIME_300;


    public NoDoubleClick(int intervalsTime) {
        this.intervalsTime = intervalsTime;
    }

    public boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > 0 && currentTime - lastClickTime < intervalsTime) {
            return true;
        } else {
            lastClickTime = currentTime;
            return false;
        }
    }

}
