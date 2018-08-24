package com.dysen.image;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dysen.common_library.utils.FileUtils;
import com.dysen.common_library.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    Button btnJp;
    Button btnShare;
    private String imagePath;
    private ImageView img;
    private FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {

        img = findViewById(R.id.iv);
        btnJp = findViewById(R.id.btn_jp);
        btnShare = findViewById(R.id.btn_share);
        //截屏
        btnJp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                screenshot();
                ScreenShot.share(Main2Activity.this, "hello");
            }
        });
        //分享
        btnShare.setOnClickListener(new View.OnClickListener() {
            //过滤出需要分享到对应的平台：微信好友、朋友圈、QQ好友。  可自行修改
            String[] targetApp = new String[]{"com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mobileqq.activity.JumpActivity"};

            @Override
            public void onClick(View v) {
                sharePic();
            }
        });
    }

    /*  1 com.android.email
        2 packageName: com.android.mms
        3 packageName: com.example.android.notepad
        4 packageName: com.huawei.android.wfdft
        5 packageName: com.huawei.fans
        6 packageName: com.huawei.fans
        7 packageName: com.huawei.health
        8 packageName: com.huawei.hidisk
        9 packageName: com.huawei.hwid
        10 packageName: com.UCMobile*/
    private void sharePic() {
        if (imagePath != null) {
            File file = new File(imagePath);
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            intent.setType("image/*");// 分享发送的数据类型
            PackageManager packageManager = Main2Activity.this.getPackageManager();
            List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, 0);
            if (!resolveInfo.isEmpty()) {
                List<Intent> targetedShareIntents = new ArrayList<Intent>();
                for (ResolveInfo info : resolveInfo) {
                    ActivityInfo activityInfo = info.activityInfo;

                    // judgments : activityInfo.packageName, activityInfo.name, etc.
                    if (activityInfo.packageName.contains("mms") || activityInfo.name.contains("mms")
                            || activityInfo.packageName.contains("notepad") || activityInfo.name.contains("notepad")
                            || activityInfo.packageName.contains("huawei") || activityInfo.name.contains("huawei")
                            || activityInfo.packageName.contains("bluetooth") || activityInfo.name.contains("bluetooth")
                            || activityInfo.packageName.contains("email") || activityInfo.name.contains("email")
                            || activityInfo.packageName.contains("UCMobile") || activityInfo.name.contains("UCMobile")) {
                        continue;
                    }else if (activityInfo.packageName.contains("tencent") || activityInfo.name.contains("tencent")) {
                        intent.setPackage(activityInfo.packageName);
                        targetedShareIntents.add(intent);
                        LogUtils.e("packageName---2","packageName:" +info.activityInfo.packageName);
                    }
                }
                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
                if (chooserIntent == null) {
                    return;
                }
                try {
                    chooserIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));// 分享的内容;
                    startActivity(chooserIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "Can't find share component to share", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(Main2Activity.this, "先截屏，再分享", Toast.LENGTH_SHORT).show();
        }
    }

    //截取屏幕的方法
    private void screenshot() {
        // 获取屏幕
        View cv = getWindow().getDecorView();

        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return;
        }

        // 获取状态栏高度
        Rect rect = new Rect();
        cv.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        int widths = bmp.getWidth();
        int heights = bmp.getHeight();
        int navBarHeights = heights - rect.bottom;
        // 去掉状态栏
        bmp = Bitmap.createBitmap(cv.getDrawingCache(), 0, statusBarHeights,
                widths, heights - statusBarHeights - navBarHeights);

        bmp.setHasAlpha(false);
        bmp.prepareToDraw();
        bmp = ImageUtils2.addPic(Main2Activity.this, bmp);
        bmp = ImageUtils2.scaleWithWH(bmp, bmp.getWidth(), bmp.getHeight());
        ImageUtils2.saveCroppedImage(bmp);
        img.setImageBitmap(bmp);

/**
 * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
 */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        imagePath = FileUtils.getSDdir("astest.png").getPath();
        FileUtils.saveBitmap(bmp, FileUtils.getSDdir("astest.png"));
    }
}
