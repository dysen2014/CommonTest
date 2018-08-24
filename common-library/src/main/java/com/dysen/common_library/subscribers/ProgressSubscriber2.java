package com.dysen.common_library.subscribers;

import android.content.Context;
import android.text.TextUtils;

import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.utils.LogUtils;
import com.dysen.common_library.utils.Utils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * xiezuofei
 * 2017-12-01 10:20
 * 793169940@qq.com
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressSubscriber2<T> implements Observer<T>,ProgressCancelListener{
    private SubscriberOnNextListener2 mSubscriberOnNextListener;
    private SubscriberNextOrErrorListener2 mSubscriberOnErrorListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    private boolean is_unsubscribe=false;
    private boolean is_progress_show=true;
    private boolean is_showMessage=true;
    public ProgressSubscriber2(SubscriberOnNextListener2 mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this,is_unsubscribe);
    }
    public ProgressSubscriber2(SubscriberNextOrErrorListener2 mSubscriberOnErrorListener, Context context) {
        this.mSubscriberOnErrorListener = mSubscriberOnErrorListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this,is_unsubscribe);
    }
    public ProgressSubscriber2(SubscriberOnNextListener2 mSubscriberOnNextListener, Context context, boolean is_showMessage) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.is_showMessage=is_showMessage;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public ProgressSubscriber2(SubscriberOnNextListener2 mSubscriberOnNextListener, Context context, boolean is_showMessage, boolean is_progress_show) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.is_progress_show=is_progress_show;
        this.is_showMessage=is_showMessage;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
    }
    public ProgressSubscriber2(SubscriberNextOrErrorListener2 mSubscriberOnErrorListener, Context context, boolean is_showMessage, boolean is_progress_show) {
        this.mSubscriberOnErrorListener = mSubscriberOnErrorListener;
        this.context = context;
        this.is_progress_show=is_progress_show;
        this.is_showMessage=is_showMessage;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, is_unsubscribe);
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
    public void onNext(@NonNull T t) {
//        WorldIpDao.setDomainReset();
        LogUtils.e("onNext" ,t+"");
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
        if (mSubscriberOnErrorListener != null) {
            mSubscriberOnErrorListener.onNext(t);
        }
    }
    @Override
    public void onError(Throwable t) {
        String srt_message="";
        if(!Utils.isNetWorkConnected(context)){
            srt_message="网络中断，请检查您的网络状态";
        }else if (t instanceof ConnectException) {
            srt_message="请求超时，请检查您的网络状态";
        }else if (t instanceof SocketTimeoutException ||t instanceof HostnameVerifier) {
            srt_message="响应超时，请检查您的网络状态";
//            WorldIpDao.setDomainCount();
        }else if (t instanceof UnknownHostException || t instanceof SSLPeerUnverifiedException) {
            srt_message="无法访问，请检查您的网络状态";
//            WorldIpDao.setDomainCount();
        }else if(!TextUtils.isEmpty(t.getMessage())){
            String[] sourceStrArray = t.getMessage().split("__");
            srt_message=sourceStrArray[0];
        }
        LogUtils.e("onError" ,t.getMessage());
        LogUtils.e("onError" ,srt_message);
        if(is_showMessage){
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