package com.dysen.common_library.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author SoMustYY
 * @create 2018/9/17 下午9:27
 * @organize 卓世达科
 * @describe
 * @update
 */
public class SoftInputUtil {
    /**
     * 打开软键盘
     *
     * @param view
     *            输入框
     * @param mContext
     *            上下文
     */

    public static void openKeyBord(View view, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param view
     *            输入框
     * @param mContext
     *            上下文
     */
    public static void closeKeyBord(View view, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }





}
