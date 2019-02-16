package com.dysen.alarm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dysen.alarm.AppContext;
import com.dysen.alarm.LongRunningService;
import com.dysen.alarm.R;
import com.dysen.common_library.utils.FileUtils;
import com.dysen.common_library.utils.StatusBarUtils;
import com.dysen.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    private int type = 0;
    private Intent intent;
    private String read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //透明状态栏
        StatusBarUtil.setTransparent(this);
        intent = new Intent(this, LongRunningService.class);
        intent.putExtra("type", type);

        // 启动服务的地方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.tv})
    public void onViewClicked(View view) {
        read = FileUtils.read(this, AppContext.START_COUNT);
        if (read != null && !read.isEmpty()) {
//            setText(tv, read);
            ToastUtils.show("🚎\t\t"+read);
        }
    }

}
