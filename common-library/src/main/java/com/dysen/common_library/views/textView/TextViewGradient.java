package com.dysen.common_library.views.textView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @package com.dysen.common_library.views.textView
 * @email dy.sen@qq.com
 * created by dysen on 2019/3/13 - 10:55
 * @info    过长渐隐
 */
@SuppressLint("AppCompatCustomView")
public class TextViewGradient extends TextView {
    /**
     * 开始渐隐长度
     */
    private float mGradientScope = 200;
    /**
     * 渲染，主要实现api
     */
    private Shader shader_horizontal;

    public TextViewGradient(Context context) {
        this(context, null);
    }

    public TextViewGradient(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置渐隐长度
     */
    public void setGradientScope(float gradientScope) {
        this.mGradientScope = gradientScope;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getMeasuredWidth() > mGradientScope) {
            shader_horizontal = new LinearGradient(0, 0, mGradientScope, 0,
                    Color.parseColor("#ff0375b2"), Color.TRANSPARENT,
                    Shader.TileMode.CLAMP);
        } else {
            shader_horizontal = new LinearGradient(0, 0, mGradientScope, 0,
                    Color.parseColor("#ff0375b2"), Color.TRANSPARENT,
                    Shader.TileMode.CLAMP);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setShader(shader_horizontal);
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = getMeasuredHeight() / 2 + dy;
        canvas.drawText((String) getText(), 0, y, getPaint());
    }
}
