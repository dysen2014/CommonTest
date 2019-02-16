package com.dysen.alarm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dysen.alarm.R;
import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.utils.StatusBarUtils;
import com.dysen.svga.SVGAImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_svga)
    SVGAImageView ivSvga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        ButterKnife.bind(this);
        //透明状态栏
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.kbg);
        StatusBarUtil.setTransparent(this);
getWindow().setBackgroundDrawableResource(R.color.kbg);
        //实现淡入淡出的效果
//		overridePendingTransition(android.R.anim.slide_in_left,android.
//				R.anim.slide_out_right);

        //类似 iphone 的进入和退出时的效果
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.1f, 0.9f);
        aa.setDuration(2500);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });
    }
}
