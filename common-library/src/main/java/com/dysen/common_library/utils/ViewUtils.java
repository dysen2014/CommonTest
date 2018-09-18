package com.dysen.common_library.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dysen.common_library.R;

import java.util.List;

/**
 * Created by dy on 2016-08-26.
 */
public class ViewUtils {

    public ViewUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     *
     * @param context
     * @param resId
     * @param source  "<font color='#FFFFFF'><big><big>" + 0 + "</big></big></font>"
     * @return
     */
    public static Spanned toHtml(Context context, @StringRes int resId, String source) {
        Spanned html = Html.fromHtml(context.getString(resId, source));

        return html;
    }

    /**
     * 设置view的左上右下图标
     *
     * @param context
     * @param view
     * @param drawableId
     * @param orientationIndex 0 left, 1 top, 2 right, 3 bottom
     */
    public static void setViewOrientationBg(Context context, View view, @DrawableRes @ColorRes int drawableId, int orientationIndex) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (view instanceof TextView) {
            switch (orientationIndex) {
                case 0:
                    ((TextView) view).setCompoundDrawables(drawable, null, null, null);
                    break;
                case 1:
                    ((TextView) view).setCompoundDrawables(null, drawable, null, null);
                    break;
                case 2:
                    ((TextView) view).setCompoundDrawables(null, null, drawable, null);
                    break;
                case 3:
                    ((TextView) view).setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        } else if (view instanceof Button) {
            switch (orientationIndex) {
                case 0:
                    ((Button) view).setCompoundDrawables(drawable, null, null, null);
                    break;
                case 1:
                    ((Button) view).setCompoundDrawables(null, drawable, null, null);
                    break;
                case 2:
                    ((Button) view).setCompoundDrawables(null, null, drawable, null);
                    break;
                case 3:
                    ((Button) view).setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        } else if (view instanceof EditText) {
            switch (orientationIndex) {
                case 0:
                    ((EditText) view).setCompoundDrawables(drawable, null, null, null);
                    break;
                case 1:
                    ((EditText) view).setCompoundDrawables(null, drawable, null, null);
                    break;
                case 2:
                    ((EditText) view).setCompoundDrawables(null, null, drawable, null);
                    break;
                case 3:
                    ((EditText) view).setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        } else {

        }
    }

    /**
     * 设置 TextView, Button, EditText等的内容
     *
     * @param context
     * @param value
     * @param object
     */
    public static void setText(Context context, String value, Object object) {

        if (object instanceof TextView) {
            ((TextView) object).setText(value);
        } else if (object instanceof Button) {
            ((Button) object).setText(value);
        } else if (object instanceof EditText) {
            ((EditText) object).setText(value);
        } else {

        }
    }

    public static void setText(String value, Object object) {

        if (object instanceof TextView) {
            ((TextView) object).setText(value);
        } else if (object instanceof Button) {
            ((Button) object).setText(value);
        } else if (object instanceof EditText) {
            ((EditText) object).setText(value);
        } else {

        }
    }

    public static void setHint(String value, Object object) {

        if (object instanceof TextView) {
            ((TextView) object).setHint(value);
        } else if (object instanceof Button) {
            ((Button) object).setHint(value);
        } else if (object instanceof EditText) {
            ((EditText) object).setHint(value);
        } else {

        }
    }

    /**
     * 获取 TextView, Button, EditText等的内容
     *
     * @param object
     * @return
     */
    public static String getText(Object object) {

        if (object instanceof TextView) {
            return ((TextView) object).getText().toString();
        } else if (object instanceof Button) {
            return ((Button) object).getText().toString();
        } else if (object instanceof EditText) {
            return ((EditText) object).getText().toString();
        } else if (object instanceof String) {
            return (String) object;
        }
        return "";
    }

    public static String getHint(Object object) {

        if (object instanceof TextView) {
            return ((TextView) object).getHint().toString();
        } else if (object instanceof Button) {
            return ((Button) object).getHint().toString();
        } else if (object instanceof EditText) {
            return ((EditText) object).getHint().toString();
        } else {

        }
        return "";
    }

    /**
     * 设置text 空值 hint 显示
     *
     * @param viewList
     * @param valueList
     */
    public static void setViewsValue(List<View> viewList, List<String> valueList) {
        for (int i = 0; i < viewList.size(); i++) {
            ViewUtils.setText("", viewList.get(i));
            ViewUtils.setHint(valueList.get(i), viewList.get(i));
        }
    }

    public static void setIsVISIBLE(View view, int visibility) {

        view.setVisibility(visibility);
    }

    public static void setViewEnable(Context context, Object object, boolean flag) {

        if (object instanceof TextView) {
            if (flag) {
//                ((TextView) object).setBackgroundResource(R.mipmap.btn_bg_red);
                ((TextView) object).setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView) object).setTextColor(context.getResources().getColor(R.color.white));
            } else {
                ((TextView) object).setBackgroundResource(R.drawable.btn_gray_bg);
                ((TextView) object).setTextColor(context.getResources().getColor(R.color.txt_color));
            }
            ((TextView) object).setEnabled(flag);
        } else if (object instanceof Button) {
            if (flag) {
//                ((Button) object).setBackgroundResource(R.mipmap.btn_bg_red);
                ((Button) object).setBackgroundResource(R.drawable.btn_blue_bg);
                ((Button) object).setTextColor(context.getResources().getColor(R.color.white));
            } else {
                ((Button) object).setBackgroundResource(R.drawable.btn_gray_bg);
                ((Button) object).setTextColor(context.getResources().getColor(R.color.txt_color));
            }
            ((Button) object).setEnabled(flag);
        } else if (object instanceof EditText) {
            if (flag) {
//                ((EditText) object).setBackgroundResource(R.mipmap.btn_bg_red);
                ((EditText) object).setBackgroundResource(R.drawable.btn_blue_bg);
                ((EditText) object).setTextColor(context.getResources().getColor(R.color.white));
            } else {
                ((EditText) object).setBackgroundResource(R.drawable.btn_gray_bg);
                ((EditText) object).setTextColor(context.getResources().getColor(R.color.txt_color));
            }
            ((EditText) object).setEnabled(flag);
        } else {

        }
    }



    /**
     * 设置固定高度（如果在运行过程中才能决定ListView高度）
     * @param view
     * @param height
     */
    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams params =  view.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height;
        view.setLayoutParams(params);
    }

    /**
     * 中动态设置高度（让ListView高度最大 显示完全所有数据）
     * @param adapter
     * @param view
     */
    public static void setHeight(Adapter adapter, ListView view) {

        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, view);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = totalHeight + (view.getDividerHeight() * (adapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        view.setLayoutParams(params);
    }

    /**
     * 动态设置显示行数
     * @param adapter
     * @param view
     * @param lineCount
     */
    public static void setHeight(Adapter adapter, ListView view, int lineCount) {

        int totalHeight = 0;
        for (int i = 0; i <= lineCount; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, view);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = totalHeight + (view.getDividerHeight() * (adapter.getCount() - 1));

        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        view.setLayoutParams(params);
    }
}

