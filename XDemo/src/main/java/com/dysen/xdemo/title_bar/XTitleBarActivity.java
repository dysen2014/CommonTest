package com.dysen.xdemo.title_bar;

import android.os.Bundle;
import android.view.View;

import com.dysen.title_bar.OnTitleBarListener;
import com.dysen.title_bar.TitleBar;
import com.dysen.title_bar.style.TitleBarLightStyle;
import com.dysen.toast.ToastUtils;
import com.dysen.xdemo.R;
import com.dysen.xdemo.XBaseActivity;

public class XTitleBarActivity extends XBaseActivity {

    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 建议在Application中初始化
//        ToastUtils.init(getApplication());

        // 在这里可以初始化样式
        TitleBar.initStyle(new TitleBarLightStyle(this));
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_xtitle_bar);

        setText(tvTitle, "");
        mTitleBar = (TitleBar) findViewById(R.id.tb_main_title_bar);
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {
                ToastUtils.show("左项View被点击");
            }

            @Override
            public void onTitleClick(View v) {
                ToastUtils.show("中间View被点击");
            }

            @Override
            public void onRightClick(View v) {
                ToastUtils.show("右项View被点击");
            }
        });
    }

    @Override
    public void setListener() {

    }
}
