package com.dysen.test;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.bean.CommonBean;
import com.dysen.common_library.bean.TestBean;
import com.dysen.common_library.http.HttpMethods;
import com.dysen.common_library.http.HttpResult;
import com.dysen.common_library.subscribers.ProgressSubscriber;
import com.dysen.common_library.subscribers.ProgressSubscriber2;
import com.dysen.common_library.subscribers.SubscriberNextOrErrorListener;
import com.dysen.common_library.subscribers.SubscriberNextOrErrorListener2;
import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.utils.Tools;
import com.dysen.common_library.views.CustomPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_get)
    Button btnGet;

    private CustomPopWindow mCustomPopWindow;
    String url = "https://wd9674052776zkawmw.wilddogio.com/";
    private SubscriberNextOrErrorListener2<TestBean> subscriberNextOrErrorListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_test);
        ButterKnife.bind(this);

    }

    @Override
    public void initView() {
        subscriberNextOrErrorListener2 = new SubscriberNextOrErrorListener2<TestBean>() {

            @Override
            public void onNext(TestBean testBean) {
//                UIHelper.ToastMessage(TestActivity.this, testBean.getResMsg().getCode());
                Tools.showConfirmDialog(TestActivity.this, testBean + "======" + testBean.getName());
            }

            @Override
            public void onError(String error) {
                UIHelper.ToastMessage(TestActivity.this, error);

            }
        };
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.btn_up, R.id.btn_down, R.id.btn_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                showPopMenu(R.layout.activity_main, view);
                break;
            case R.id.btn_down:
                showPopMenu(R.layout.activity_main, view);
                break;
            case R.id.btn_get:
                getVersion();
                break;
        }
    }

    private void getVersion() {
//        HttpMethods.getInstanceTrans(url).getAppVersion(new ProgressSubscriber2<TestBean>(subscriberNextOrErrorListener2, this, false, true));

    }

    /**
     * 选择器
     *
     * @param view
     */
    private void showPopMenu(int resource, View view) {
        View contentView = LayoutInflater.from(mContext).inflate(resource, null);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .size(-1, -1)
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create();
//        mCustomPopWindow.showAsDropDown(view, 0, -(view.getHeight() + mCustomPopWindow.getHeight()));
        //处理popWindow 显示内容
        handleLogic(contentView, view);
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     * @param view
     */
    private void handleLogic(View contentView, View view) {
        if (view.getId() == R.id.btn_up) {
            mCustomPopWindow.showAsDropDown(view, 0, -(view.getHeight() + mCustomPopWindow.getHeight()), Gravity.TOP);
        } else if (view.getId() == R.id.btn_down) {
            mCustomPopWindow.showAsDropDown(view, 0, 0, Gravity.BOTTOM);
        }
        mCustomPopWindow.getPopupWindow().setAnimationStyle(R.style.pop_animation);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
//                    case R.id.ids:
//                        break;
                }
            }
        };
//        contentView.findViewById(R.id.ids).setOnClickListener(listener);
    }
}
