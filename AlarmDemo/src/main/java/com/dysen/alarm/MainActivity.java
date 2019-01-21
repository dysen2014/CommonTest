package com.dysen.alarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.changeskin.SkinManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.iv)
    ImageView iv;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().register(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent = new Intent(MainActivity.this, LongRunningService.class);
        intent.putExtra("type", type);

        // 启动服务的地方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }

    @OnClick({R.id.tv, R.id.iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv:
                type = ++type % 2;
                break;
            case R.id.iv:
                type = ++type % 2;
                break;
        }
        if (type == 0)
            SkinManager.getInstance().changeSkin("red");
        else
            SkinManager.getInstance().changeSkin("green");
    }
}
