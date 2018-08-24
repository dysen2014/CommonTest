package com.dysen.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dysen.authenticator.AppContext
import com.dysen.authenticator.AuthenticatorActivity
import com.dysen.common_library.base.BaseActivity
import com.dysen.common_library.bean.CommonBean
import com.dysen.common_library.http.HttpMethods
import com.dysen.common_library.http.HttpResult
import com.dysen.common_library.subscribers.ProgressSubscriber
import com.dysen.common_library.subscribers.SubscriberNextOrErrorListener
import com.dysen.common_library.ui.UIHelper
import com.dysen.common_library.utils.Tools
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSetContentView(R.layout.activity_main)

        btn_start_authenticator.setOnClickListener(this)
        btn_start_gesture.setOnClickListener(this)
        btn_pop.setOnClickListener(this)
        btn_scan.setOnClickListener(this)
    }

    override fun onClick(view: View?) {//方式2
        when (view?.id) {
            R.id.btn_start_authenticator -> {
                transAty(AuthenticatorActivity::class.java)
            }
            R.id.btn_start_gesture -> {
//                jumpGesturte()
                val dialog = Tools.showConfirmDialog(this, "你好👋 这是一个弹框！", this, this)
                dialog.getmDialog().setCanceledOnTouchOutside(false)
            }
            R.id.btn_pop -> {

                transAty(TestActivity::class.java)
            }
            R.id.btn_scan -> {

                scanBarcode()
            }
        }
    }

    /**
     * 启动扫描QR码
     */
    private fun scanBarcode() {
        AppContext.customScan(this)
    }

    override// 通过 onActivityResult的方法获取 扫描回来的 值
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            if (intentResult.contents == null) {
                //UIHelper.ToastMessage(context,getString(R.string.asset_nrwk));
            } else {
                val content = intentResult.contents
                UIHelper.ToastMessage(this, content)
//                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun jumpGesturte() {

//        val user_lock = SharedPreUtils.getInstance(mContext).get(AppContext.KEY_USER_LOCK, "")

//        if ("1".equals(user_lock))
//        //手势密码
//            UIHelper.showGesture(this, GestureAty.TYPE_UNLOCK)
//
//        btn_start_gesture.setOnClickListener {
//            if ("".equals(user_lock)) {
//                UIHelper.showGestureHint(this)
//            } else {
//                UIHelper.showGestureSet(this)
//            }
//        }
    }

    fun transAty(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }
}
