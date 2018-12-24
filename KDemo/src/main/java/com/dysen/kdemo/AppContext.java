package com.dysen.kdemo;

import android.app.Application;
import android.content.Context;
import android.text.ClipboardManager;

import com.dysen.kdemo.utils.Tools;

/**
 * @package com.dysen.kdemo
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/22 - 3:12 PM
 * @info
 */
public class AppContext extends Application {
    private static AppContext app;

    public static AppContext getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public void copy(String content, Context context) {
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            Tools.toast(Tools.getString(R.string.user_fzcg));
        } catch (Exception e) {

        }
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public String paste(Context context) {
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String str = "";
            if (cmb.getText() != null) {
                str = cmb.getText().toString().trim();
            }
            if (str.equals("")) {
                Tools.toast(Tools.getString(R.string.user_ztsb));
            }
            return str;
        } catch (Exception e) {
            return "";
        }

    }
}
