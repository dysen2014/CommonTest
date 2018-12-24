package com.dysen.common_library.views;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
/**
 * @package com.vip.zb.view
 * @email dy.sen@qq.com
 * created by dysen on 2016/12/11 - 10:30 AM
 * @info
 */
public class TextViewMarquee extends android.support.v7.widget.AppCompatTextView {

	public TextViewMarquee(Context context) {
		super(context);
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setSingleLine(true);
	}
	
	public TextViewMarquee(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setSingleLine(true);
	}

	public TextViewMarquee(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setSingleLine(true);
	}
	

	@Override
    public boolean isFocused() {
        return true;
    }

}
