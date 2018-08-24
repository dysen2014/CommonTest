package com.dysen.common_library.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.dysen.common_library.R;


public class LoadingProgress {
	public Dialog mDialog;
	private Context mContext;
	private ImageView spinnerImageView;
	private Animation anim;

	public LoadingProgress(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.progress_custom, null);
		mContext=context;
		mDialog = new Dialog(context, R.style.Custom_Progress);
		spinnerImageView=(ImageView) view.findViewById(R.id.spinnerImageView);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.dimAmount = 0.4f;

		anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new LinearInterpolator());//不停顿
		//anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
		anim.setRepeatCount(-1);//重复次数
		anim.setFillAfter(true);//停在最后
		anim.setDuration(2000);
		spinnerImageView.startAnimation(anim);
		mDialog.getWindow().setAttributes(lp);
	}
	public void show() {

		/*// 获取ImageView上的动画背景
		AnimationDrawable spinner = (AnimationDrawable) mDialog.findViewById(R.id.spinnerImageView)
				.getBackground();
		// 开始动画
		spinner.start();*/
		if(mDialog!=null){
			try {
				mDialog.show();
			}catch (Exception e){

			}
		}
	}

	public void dismiss() {
		if(mDialog!=null){
			try {
				mDialog.dismiss();
			}catch (Exception e){

			}


		}
	}


}
