package com.dysen.seekbar;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.utils.Tools;
import com.dysen.common_library.views.CustomPopWindow;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class MainActivity extends BaseActivity {

    @butterknife.BindView(R.id.tv)
    TextView tv;
    @butterknife.BindView(R.id.ic_seekbar)
    IndicatorSeekBar icSeekbar;
    private CustomPopWindow customPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.bind(this);

//        initView();
        setListener();
    }

    @Override
    public void initView() {
        super.initView();


    }

    private void showMenu(final View view) {
        final View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog, null); //创建并显示popWindow
        customPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        IndicatorSeekBar icSeekbar = contentView.findViewById(R.id.ic_seekbar);
        TextView textview = contentView.findViewById(R.id.tv);
        icSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                textview.setText("===222==="+seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        if (customPopWindow.getPopupWindow().isShowing())
        customPopWindow.dissmiss();
        customPopWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        customPopWindow.getPopupWindow().update();
    }

    @Override
    public void setListener() {
        tv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {

                Tools.toast("this is hello");
                showMenu(null);
                                  }
                              });

        icSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                tv.setText("======"+seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }
}
