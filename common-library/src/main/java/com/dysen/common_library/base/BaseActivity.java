package com.dysen.common_library.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.common_library.R;
import com.dysen.common_library.utils.Tools;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/23 - 下午2:07
 * @info
 */
public class BaseActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedinstancestate) {
        super.onCreate(savedinstancestate);

        setContentView(R.layout.activity_base);
        baseInit();
    }
    protected void baseInit() {
        if (mHandler == null)
            mHandler = new Handler();
        mContext = aty = this;
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvMenu = findViewById(R.id.tv_menu);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Tools.eventBusRegister(this);
    }
    /**
     * set screen view
     *
     * @param layoutResID
     */
    protected void baseSetContentView(int layoutResID) {

        vContent = findViewById(R.id.v_content); //v_content是在基类布局文件中预定义的layout区域
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //通过LayoutInflater填充基类的layout区域
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResID, null);
        vContent.addView(v, layoutParams);

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
}
