package com.dysen.common_library.subscribers;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.dysen.common_library.views.LoadingProgress;


/**
 * xiezuofei
 * 2017-12-01 13:20
 * 793169940@qq.com
 */
public class ProgressDialogHandler extends Handler {
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private Context context;
    private boolean cancelable;
    private LoadingProgress loadingProgress;
    private ProgressCancelListener mProgressCancelListener;
    public ProgressDialogHandler(){

    }
    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener, boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){
        if (loadingProgress == null) {
            loadingProgress = new LoadingProgress(context);
            loadingProgress.mDialog.setCancelable(cancelable);
            if (cancelable) {
                loadingProgress.mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if (!loadingProgress.mDialog.isShowing()) {
                loadingProgress.show();

            }
        }
    }

    private void dismissProgressDialog(){
        if (loadingProgress != null) {
            loadingProgress.dismiss();
            loadingProgress = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
