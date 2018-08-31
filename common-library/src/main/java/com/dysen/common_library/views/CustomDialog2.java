package com.dysen.common_library.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dysen.common_library.R;
import com.dysen.common_library.utils.Tools;

/**
 * @package com.dysen.common_library.views
 * @email dy.sen@qq.com
 * created by dysen on 2018/8/4 - 下午3:28
 * @info
 */
public class CustomDialog2 extends AlertDialog {
    private android.app.AlertDialog.Builder builder;
    private android.app.AlertDialog mDialog;
    
    private String[] strs;
    private Context mContext;
    private String selectStr;

    protected CustomDialog2(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    protected CustomDialog2(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected CustomDialog2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }
    public AlertDialog showCustomDialog(View view) {
        if (builder == null) {
            builder = new android.app.AlertDialog.Builder(mContext);
            builder.setTitle("选择内容：");
            mDialog = null;
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        } else {
//            if (layout == linearDeauft) {
//                strs = new String[]{getString(R.string.trans_mo)};
//            } else if (layout == linearDears) {
//                strs = new String[]{getString(R.string.dears_5), mContext.getString(R.string.dears_10), mContext.getString(R.string.dears_20), mContext.getString(R.string.dears_50)};
//            }

            /*！注意这里的方法以及参数哦！*/
            builder.setItems(strs, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int position) {
                    selectStr = strs[position];
                    /*提醒测试作用*/
                    Toast.makeText(mContext, "选择的内容为：" + strs[position],
                            Toast.LENGTH_SHORT).show();
                }
            });
            mDialog = builder.create();
            Window window = mDialog.getWindow();
            window = setDialog(window, view);//设置弹窗的位置
            mDialog.show();
            window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        }
        return mDialog;
    }

    private Window setDialog(Window mWindow, View view) {

        mWindow.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
//            linear_rate.getLocationInWindow(location);
        lp.x = location[0];
        lp.y = location[1]+ Tools.dp2px(16);
        lp.width = Tools.dp2px(120);// 设置悬浮窗口长宽数据
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(lp);
        return mWindow;
    }

    public String[] getStrs() {
        return strs;
    }

    public void setStrs(String[] strs) {
        this.strs = strs;
    }

    public String getSelectStr() {
        return selectStr;
    }

    public void setSelectStr(String selectStr) {
        this.selectStr = selectStr;
    }

    
    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View view = llLinear;
//
//            LogUtils.e("========="+inRangeOfView(view, event));
//            if (!inRangeOfView(view, event)) {
//                if (!inRangeOfView(linear_left, event) && !inRangeOfView(linear_right, event)) {
//                    LogUtils.e("======inRangeOfView==========");
//                    linear_left.setVisibility(GONE);
//                    linear_right.setVisibility(GONE);
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    //判断是否在控件区域内
//    private boolean inRangeOfView(View view, MotionEvent event) {
//        int[] location = new int[2];
////        view.getLocationOnScreen(location);
//        view.getLocationInWindow(location);
//        int x = location[0];
//        int y = location[1];
//        LogUtils.e(event.getX()+"=="+x+"=="+view.getWidth()+"=="+event.getY()+"=="+y+"=="+view.getHeight());
//        LogUtils.e((event.getX() < x) +"=="+ (event.getX() > (x + view.getWidth())) +"=="+ (event.getY() < y) +"=="+ (event.getY() > (y + view.getHeight())));
//        if (event.getX() < x || event.getX() > (x + view.getWidth()) || event.getY() < y || event.getY() > (y + view.getHeight())) {
//            return false;
//        }
//        return true;
//    }
}
