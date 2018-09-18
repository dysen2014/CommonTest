package com.dysen.common_library.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

import com.dysen.common_library.R;

/**
 * Created by integrated on 2018/4/28.
 */

public class simpleGestureListener extends GestureDetector.SimpleOnGestureListener {
    final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;
    public Context context;
    public ViewFlipper mFlipper;


    public simpleGestureListener(Context context, ViewFlipper mFlipper) {
        this.context = context;
        this.mFlipper = mFlipper;
    }

    //不知道为什么，不加上onDown函数的话，onFling就不会响应，真是奇怪
    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
//        toast("ondown");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // Fling left
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

            if (mFlipper != null) {

                mFlipper.setInAnimation(context, R.anim.next_in);
                mFlipper.setOutAnimation(context, R.anim.next_out);
                mFlipper.showNext();
            }

//        toast("Fling Left");
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            if (mFlipper != null) {
                mFlipper.setInAnimation(context, R.anim.quit_in);
                mFlipper.setOutAnimation(context, R.anim.quit_out);
                mFlipper.showPrevious();
            }

//        toast("Fling Right");
        }
        //还原轮播是的动画效果
        mFlipper.setInAnimation(context, R.anim.next_in);
        mFlipper.setOutAnimation(context, R.anim.next_out);
        return true;
    }
}