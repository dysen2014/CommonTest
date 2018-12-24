package com.dysen.common_library.views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.dysen.common_library.R;
import com.dysen.common_library.base.AppContext;
import com.dysen.common_library.utils.Tools;
/**
 * @package com.vip.zb.view
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/15 - 10:30 AM
 * @info
 */
public class WrappedWebView extends WebView {
    public WrappedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof Activity) {
            initWebViewSettings((Activity) context, this);
        }
    }

    public WrappedWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (context instanceof Activity) {
            initWebViewSettings((Activity) context, this);
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void loadDataWithBaseURL(String data) {

        if (TextUtils.isEmpty(data))
            return;

        String mDataBackup = new String(data);

        try {
            StringBuffer mWebStyle = new StringBuffer();
            mWebStyle.append("<style>{font-size:18px;line-height:150%} p {color:#333;} a {color:#3E62A6;}");
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                mWebStyle.append(" img {width:100%;}");

            } else {
                mWebStyle.append(" img {width:").append((Tools.getScreenWidth() - Tools.dp2px(20))).append("px;}");

            }

            mWebStyle.append(" </style>");

            if (data.contains("<img")) {
                data = data.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
                data = data.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
                data = data.replaceAll("<img", "<img onclick=\"window.imagelistner.openImage(this.src)\"");

                getSettings().setJavaScriptEnabled(true);
            }

			/*String linkCss = "<link rel=\"stylesheet\" href=\"file:///android_asset/style.css\" type=\"text/css\"/>";
			String content = linkCss + result;*/
            mWebStyle.append(data);
            loadDataWithBaseURL("about:blank", mWebStyle.toString(), "text/html", "UTF-8", null);

        } catch (Exception e) {
            Tools.printStackTrace("NLWebView", e);
            loadDataWithBaseURL("about:blank", mDataBackup, "text/html", "UTF-8", null);
        }
    }

    public static void initWebViewSettings(final Activity activity, WebView webView) {
        webView.setBackgroundColor(Tools.getColor(R.color.white));
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(false);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
    }



}
