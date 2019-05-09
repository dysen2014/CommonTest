package com.dysen.common_library.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.dysen.common_library.R;
import com.dysen.common_library.adapter.viewpager.BasePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ShowTipDialog {
    ViewPager viewPager;
    private Dialog mDialog;
    private Context mContext;
    private View mContentView;
    private View.OnClickListener btnNoOnClickListener, btnYesOnClickListener;
    private BasePagerAdapter.ViewAdapter mAdapter;
    private List<View> listviews = new ArrayList<>();
    private BezierBannerDot bezierBannerDot;

    public ShowTipDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context, R.style.Custom_Progress);
        mContentView = LayoutInflater.from(context).inflate(R.layout.layout_common_dialog, null);

        initView();
        initDialog();
        initData();
    }

    private void initData() {
        if (listviews == null)
            listviews = new ArrayList<>();
        //充填数据
        mAdapter = new BasePagerAdapter.ViewAdapter(listviews);
        viewPager.setAdapter(mAdapter);
        bezierBannerDot.attachToViewpager(viewPager);
    }

    private void initDialog() {
        mDialog.setContentView(mContentView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
    }

    private void initView() {
        bezierBannerDot = mContentView.findViewById(R.id.bd);
        viewPager = mContentView.findViewById(R.id.view_pager);
    }

    public void setListviews(List<View> views) {
        listviews.clear();
        listviews.addAll(views);
        mAdapter.notifyDataSetChanged();
        bezierBannerDot.attachToViewpager(viewPager);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public BezierBannerDot getBezierBannerDotrView() {
        return bezierBannerDot;
    }

    public View getView() {
        return mContentView;
    }

    public void setNoOnClickListener(View.OnClickListener mListener) {
        this.btnNoOnClickListener = mListener;
    }

    public ShowTipDialog show() {
        if (mDialog != null) {
            mDialog.show();
        }

        return this;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public final <T extends View> T findView(int id) {
        return mContentView.findViewById(id);
    }
}
