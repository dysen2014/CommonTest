package com.dysen.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dysen.common_library.utils.FileUtils;

public class ImageDemo extends AppCompatActivity {

    private TextView button;

    private ScrollView mScrollView;

    private ImageView mImage;

    private LinearLayout mBg, llTxt;

    private ImageUtils2 utils;
    Activity aty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_demo);
        aty = this;
        utils = new ImageUtils2(ImageDemo.this);
        initView();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = captureScreen(aty);

//                Bitmap bitmap = utils.getBitmap(ImageDemo.this, R.drawable.ic_launcher_background);//这里返回的是没有水印的图片
//                Bitmap bitmap = utils.getBitmapByView(mScrollView);//这里返回的是没有水印的图片
                mImage.setVisibility(View.VISIBLE);

//                utils.startAnim(mBg, mImage, bitmap);

                bitmap = ImageUtils2.addPic(ImageDemo.this, bitmap);//返回有图片水印

//                bitmap=utils.drawTextToBitmap(bitmap, "http://blog.csdn.net/qq_25193681");//返回有文字的水印

                utils.saveCroppedImage(bitmap);

                mImage.setImageBitmap(bitmap);
//                mImage.setVisibility(View.VISIBLE);

            }
        });

    }

    /**
     * 获取整个窗口的截图
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    private Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();

        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return null;
        }

        bmp.setHasAlpha(false);
        bmp.prepareToDraw();
        return bmp;
    }

        private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.mScrollview);
        button = (TextView) findViewById(R.id.save);
        mBg = (LinearLayout) findViewById(R.id.image_bg);
        llTxt = (LinearLayout) findViewById(R.id.ll_txt);
        mImage = (ImageView) findViewById(R.id.jietu_image);
    }
}
