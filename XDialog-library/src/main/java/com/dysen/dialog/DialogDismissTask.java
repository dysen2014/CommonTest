package com.dysen.dialog;

import java.util.TimerTask;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/ToastUtils
 *    time   : 2019/01/04
 *    desc   : Toast 定时销毁任务
 */
final class DialogDismissTask extends TimerTask {

    private XDialog mToast;

    DialogDismissTask(XDialog toast) {
        mToast = toast;
    }

    @Override
    public void run() {
        if (mToast != null && mToast.isShow()) {
            mToast.cancel();
        }
    }
}
