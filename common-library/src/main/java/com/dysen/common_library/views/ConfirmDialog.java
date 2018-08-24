package com.dysen.common_library.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dysen.common_library.R;


public class ConfirmDialog implements View.OnClickListener {

    private Dialog mDialog;
    private Context mContext;
    private View mContentView;
    private TextView txtTitle, txtContent;
    private Button btnNo, btnYes;
    private View.OnClickListener btnNoOnClickListener, btnYesOnClickListener;

    public ConfirmDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context, R.style.Custom_Progress);
        mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);

        initDialog();
        initView();
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
        txtTitle = findView(R.id.txt_title);
        txtContent = findView(R.id.txt_content);
        btnNo = findView(R.id.btn_no);
        btnYes = findView(R.id.btn_yes);
        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }

    public Dialog getmDialog() {
        return mDialog;
    }


    public TextView getTitleView() {
        return txtTitle;
    }

    public TextView getContentView() {
        return txtContent;
    }

    public Button getBtnYes() {
        return btnYes;
    }

    public Button getBtnNo() {
        return btnNo;
    }

    public void setBtnNoGone() {
        btnNo.setVisibility(View.GONE);
    }

    public void setBtnNoVisible() {
        btnNo.setVisibility(View.VISIBLE);
    }

    public void setYesOnClickListener(View.OnClickListener mListener) {
        this.btnYesOnClickListener = mListener;
    }

    public void setNoOnClickListener(View.OnClickListener mListener) {
        this.btnNoOnClickListener = mListener;
    }

    public ConfirmDialog show() {
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

    public final <T extends View> T findView(int id) {
        return mContentView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_no) {
            if (btnNoOnClickListener != null) {
                btnNoOnClickListener.onClick(v);
            }
            dismiss();

        } else if (i == R.id.btn_yes) {
            if (btnYesOnClickListener != null) {
                btnYesOnClickListener.onClick(v);
            }
            dismiss();

        }
    }
}
