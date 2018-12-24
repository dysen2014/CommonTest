package com.dysen.kdemo.kline;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dysen.kdemo.R;

/**
 * @package com.vip.zb.fragment
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/17 - 下午6:10
 * @info
 */
public class KIntroductionFragment extends Fragment {

    private static Context mContext;
    private View mView;

    private WebView mWebView;
    private TextView cointitle;

    public static KIntroductionFragment newInstance(Context context) {
        mContext = context;
        Bundle args = new Bundle();

        KIntroductionFragment fragment = new KIntroductionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_fragment_k_introduction, null);
        initView();
        return mView;
    }

    public void init(String exchangeType, String currencyType) {
        if (isAdded())
            getCoinData();
    }

    public void initView() {

        mWebView = (WebView) mView.findViewById(R.id.webview);
        cointitle = (TextView) mView.findViewById(R.id.cointitle);
        cointitle.setText(mContext.getString(R.string.coin_data_title));
        getCoinData();
    }

    //获取币种资料
    public void getCoinData() {
        mWebView.loadUrl("file:///android_asset/coin_data.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //支持App内部javascript交互
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        //自适应屏幕
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//
//        //设置可以支持缩放
//        mWebView.getSettings().setSupportZoom(true);
//
//        //扩大比例的缩放
//        mWebView.getSettings().setUseWideViewPort(true);
//
//        //设置是否出现缩放工具
//        mWebView.getSettings().setBuiltInZoomControls(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
