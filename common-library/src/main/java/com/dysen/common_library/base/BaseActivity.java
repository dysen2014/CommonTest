package com.dysen.common_library.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.common_library.R;
import com.dysen.common_library.adapter.recycler.StringUtils;
import com.dysen.common_library.utils.StatusBarUtil;
import com.dysen.common_library.utils.Tools;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/23 - 下午2:07
 * @info
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseCallback {

    /**
     * the tag for log messages
     */
    protected String TAG = getClass().getSimpleName();
    BaseActivity aty;
    protected Context mContext;
    protected Handler mHandler;

    protected TextView tvBack;
    protected TextView tvTitle;
    protected TextView tvMenu;
    protected LinearLayout vContent;
    protected LinearLayout llBg;
    protected LinearLayoutCompat llTitleBg;
    protected String defaultContent = "";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedinstancestate) {
        super.onCreate(savedinstancestate);

        setContentView(getLayoutId());

        baseInit();

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        llTitleBg.setBackgroundColor(Tools.getColor(R.color.transparent));
        initView();
    }

    /**
     * 修改状态栏的夜色
     * @param colorId
     */
    protected void setStatusColor(int colorId){

        if (llBg == null)
            llBg = findViewById(R.id.ll_bg);
        llBg.setBackgroundColor(Tools.getColor(colorId));
        StatusBarUtil.setTransparent(this);
    }

    protected void baseInit() {
        if (mHandler == null)
            mHandler = new Handler();
        mContext = aty = this;
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvMenu = findViewById(R.id.tv_menu);
        llTitleBg = findViewById((R.id.ll_title_bg));
        llBg = findViewById((R.id.ll_bg));
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Tools.eventBusRegister(this);
    }

    @Override
    public void initView() {

    }


    protected BaseActivity setText(TextView textView, String content) {
        textView.setText(StringUtils.obtainNoNullText(content, defaultContent));
        return this;
    }

    /**
     * set screen view
     *
     * @param layoutResID
     */
    public void baseSetContentView(int layoutResID) {
        vContent = findViewById(R.id.v_content); //v_content是在基类布局文件中预定义的layout区域
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //通过LayoutInflater填充基类的layout区域
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResID, null);
        ButterKnife.bind(this, v);
        vContent.addView(v, layoutParams);
        setListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_base;
    }

    protected void gotoNext(Class cls, boolean... isfinish) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (isfinish.length > 0)
            if (isfinish[0])
                finish();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.eventBusUnregister(this);
    }

    protected void transAty(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void transAty(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }
}
