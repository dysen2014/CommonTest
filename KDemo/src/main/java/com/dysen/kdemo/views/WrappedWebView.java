package com.dysen.kdemo.views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.dysen.kdemo.utils.ColorUtils;
import com.dysen.kdemo.utils.Tools;

public class WrappedWebView extends WebView {
    private Context mContext;

    public WrappedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
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

        //处理图片过宽 测试在1920*1080手机上330px 刚刚好
        data = data.replaceAll("width:[0-9]*px","width:330px6");
        data = data.replaceAll("height:[0-9]*px","height:auto");
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
            loadDataWithBaseURL("", mWebStyle.toString(), "text/html", "UTF-8", "about:blank");

        } catch (Exception e) {
            loadDataWithBaseURL("", mDataBackup, "text/html", "UTF-8", "about:blank");
        }
    }

    public void initWebViewSettings(final Activity activity, WebView webView) {
        webView.setBackgroundColor(ColorUtils.getWhite(mContext));
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(false);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
    }
}
