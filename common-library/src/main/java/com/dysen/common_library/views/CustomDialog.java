package com.dysen.common_library.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * @package com.vip.zb.view
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/28 - 下午3:40
 * @info
 */
public class CustomDialog {
    private Dialog mDialog;
    private Button bt_confirm;
    private Button bt_cancel;
    Context mContext;

    public CustomDialog(Context mContext) {
        this.mContext = mContext;
    }

    public Dialog showDialog(View view) {
        //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
        mDialog = new Dialog(mContext);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //将自定义布局设置进去
        mDialog.setContentView(view);
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
//        window.setAttributes(lp);

        //设置点击其它地方不让消失弹窗
//        mDialog.setCancelable(false);
        return mDialog;
    }

    public View getResView(@LayoutRes int resource) {
        //填充布局
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(resource, null);
        return dialogView;
    }
}
