package com.dysen.common_library.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.dysen.common_library.adapter.recycler.StringUtils;

/**
 * @package com.dysen.common_library.base
 * @email dy.sen@qq.com
 * created by dysen on 2019/2/26 - 16:14
 * @info
 */
public class BaseFragment extends Fragment {

    private String defaultContent = "";

    /**
     * 检测网络
     *
     * @return
     */
    public boolean isNetConnect() {
        ConnectivityManager connectivity = (ConnectivityManager) BaseAppContext.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // 当前网络是连接的
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {   //网络或wifi
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("ResourceType")
    public int gColor(@ColorInt int colorId) {
        return getResources().getColor(colorId);
    }

    public String gString(@StringRes int stringId) {
        return getResources().getString(stringId);
    }
    public String gString(@StringRes int stringId, Object... formatArgs) {
        return getResources().getString(stringId, formatArgs);
    }
    public String[] gArrays(@ArrayRes int arrayId) {
        return getResources().getStringArray(arrayId);
    }

    protected BaseFragment sText(TextView textView, String content) {
        if (isNull(textView))
            return this;
        textView.setText(StringUtils.obtainNoNullText(content, defaultContent));
        return this;
    }

    protected BaseFragment sTextColor(TextView textView, int colorId) {
        if (isNull(textView))
            return this;
        textView.setTextColor(colorId);
        return this;
    }

    protected BaseFragment sTextSize(TextView textView, float size) {
        if (isNull(textView))
            return this;
        textView.setTextSize(size);
        return this;
    }

    public boolean isNull(Object object) {
        return object == null ? true : false;
    }
}
