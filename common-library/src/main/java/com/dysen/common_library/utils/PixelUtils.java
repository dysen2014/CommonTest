package com.dysen.common_library.utils;

import android.content.Context;

/**
 * @package com.vip.zb.tool
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/13 - 上午11:28
 * @info
 */
public class PixelUtils {

    private static Context mContext;

    public static PixelUtils newInstance(Context context) {
        mContext = context;
        PixelUtils pixelUtils = new PixelUtils();
        return pixelUtils;
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dp2px(Context mContext, int dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * px转换dp
     */
    public static int px2dp(Context mContext, int px) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * px转换sp
     */
    public static int px2sp(Context mContext, int pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转换px
     */
    public static int sp2px(Context mContext, int spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
