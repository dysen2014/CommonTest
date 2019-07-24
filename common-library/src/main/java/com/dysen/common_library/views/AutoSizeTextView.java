package com.dysen.common_library.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 文本自适应
 */
public class AutoSizeTextView extends AppCompatTextView {

    private int mMaxSize = 16, mMinSize = 4, mStepSize = 1;

    public AutoSizeTextView(Context context) {
        super(context);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getmMaxSize() {
        return mMaxSize;
    }

    public void setmMaxSize(int mMaxSize) {
        this.mMaxSize = mMaxSize;
    }

    public int getmMinSize() {
        return mMinSize;
    }

    public void setmMinSize(int mMinSize) {
        this.mMinSize = mMinSize;
    }

    public int getmStepSize() {
        return mStepSize;
    }

    public void setmStepSize(int mStepSize) {
        this.mStepSize = mStepSize;
    }

    @Override
    public int getAutoSizeMaxTextSize() {
        return getmMaxSize();
    }

    @Override
    public int getAutoSizeMinTextSize() {
        return getmMinSize();
    }

    @Override
    public int getAutoSizeTextType() {
        return TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;
    }

    @Override
    public int getAutoSizeStepGranularity() {
        return getmStepSize();
    }

    @Override
    public int getMaxLines() {
        return 1;
    }

    @Override
    public TextUtils.TruncateAt getEllipsize() {
        return TextUtils.TruncateAt.END;
    }
}
