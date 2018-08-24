package com.dysen.share_dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * @package com.dysen.common_library.utils
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/21 - 上午13:37
 * @info 截图并分享类
 */
public class MainActivity extends AppCompatActivity {

    public static String code = "";
    Button btn;
    private ScreenShotListenManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 禁止截屏（系统截屏）
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        if (!code.isEmpty()) {
            ScreenShot.shareAct2(MainActivity.this, code);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenShot.share(MainActivity.this);
            }
        });

        manager = ScreenShotListenManager.newInstance(this);

        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        // do something
                        Log.e("imagePath", "imagePath=" + imagePath);
                        ScreenShot.shareAct2(MainActivity.this, imagePath);
                    }
                }
        );

        manager.startListen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.stopListen();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenShot.onClode();
        code = "";
        finish();
    }
}