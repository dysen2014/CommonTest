package com.dysen.common_library.subscribers;

import android.content.Context;
import android.text.TextUtils;

import com.dysen.common_library.http.ResMsg;
import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.utils.LogUtils;
import com.dysen.common_library.utils.Utils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * xiezuofei
 * 2017-06-20 14:20
 * 793169940@qq.com
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressSubscriber3 implements Observer<ResMsg>,ProgressCancelListener {
    private SubscriberOnNextListener3 mSubscriberOnNextListener;
    private SubscriberNextOrErrorListener3 mSubscriberOnErrorListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    private boolean is_unsubscribe=false;
    private boolean is_progress_show=true;
    private boolean is_showMessage=true;
    public ProgressSubscriber3(SubscriberOnNextListener3 mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public ProgressSubscriber3(SubscriberNextOrErrorListener3 mSubscriberOnErrorListener, Context context) {
        this.mSubscriberOnErrorListener = mSubscriberOnErrorListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public ProgressSubscriber3(SubscriberOnNextListener3 mSubscriberOnNextListener, Context context, boolean is_showMessage) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.is_showMessage=is_showMessage;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public ProgressSubscriber3(SubscriberOnNextListener3 mSubscriberOnNextListener, Context context, boolean is_showMessage, boolean is_progress_show) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.is_progress_show=is_progress_show;
        this.is_showMessage=is_showMessage;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public ProgressSubscriber3(SubscriberNextOrErrorListener3 mSubscriberOnErrorListener, Context context, boolean is_showMessage, boolean is_progress_show) {
        this.mSubscriberOnErrorListener = mSubscriberOnErrorListener;
        this.context = context;
        this.is_progress_show=is_progress_show;
        this.is_showMessage=is_showMessage;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public void setProgressShow(boolean show)
    {
        this.is_progress_show = show;
    }
    private void showProgressDialog(){
        if (mProgressDialogHandler != null&&is_progress_show) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null&&is_progress_show) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if(is_progress_show)
        {
            showProgressDialog();
        }
    }

    @Override
    public void onNext(@NonNull ResMsg resMsg) {
        LogUtils.e("onNext" ,resMsg+"");
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(resMsg);
        }
        if (mSubscriberOnErrorListener != null) {
            mSubscriberOnErrorListener.onNext(resMsg);
        }
    }


    @Override
    public void onError(Throwable t) {
        LogUtils.e("onError" ,t.getMessage());
        String srt_message="";
        if(is_showMessage){
            if(!Utils.isNetWorkConnected(context)){
                srt_message="网络中断，请检查您的网络状态";
            }else if (t instanceof ConnectException) {
                srt_message="请求超时，请检查您的网络状态";
            }else if (t instanceof SocketTimeoutException) {
                srt_message="响应超时，请检查您的网络状态";
            }else if(!TextUtils.isEmpty(t.getMessage())){
                String[] sourceStrArray = t.getMessage().split("__");
                srt_message=sourceStrArray[0];
            }
            UIHelper.ToastMessage(context, srt_message);
        }
        if (mSubscriberOnErrorListener != null) {
            mSubscriberOnErrorListener.onError(srt_message);
        }
        if(is_progress_show)
        {
            dismissProgressDialog();
        }
    }
    @Override
    public void onComplete() {
        if(is_progress_show)
        {
            dismissProgressDialog();
        }
    }

    @Override
    public void onCancelProgress() {
        if(is_progress_show)
        {
            dismissProgressDialog();
        }
    }
}