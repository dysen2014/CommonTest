package com.dysen.kotlin_demo

import android.os.Bundle
import android.view.View
import com.dysen.kotlin_demo.common.log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dysen.common_library.base.BaseActivity
import com.dysen.common_library.utils.ImgResUtils
import com.dysen.common_library.utils.Tools
import com.dysen.kotlin_demo.async.*
import kotlinx.android.synthetic.main.activity_main.*
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/**
 * @package com.dysen.kotlin_demo
 * @email dy.sen@qq.com
 * created by dysen on 2019/1/8 - 10:31 AM
 * @info
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    var mCount: Int = 0
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv -> {
                getLogo()
                Tools.toast("Touch is get !!!®")
                mCount++
            }
            R.id.tv_ -> {
                getData()
                Tools.toast("Touch is post !!!®")
            }
        }
    }

    override fun initView() {
        super.initView()
        tvTitle.setText(R.string.app_name)
    }

    private fun getLogo() {
        var url = "http://ww3.sinaimg.cn/large/610dc034jw1fasakfvqe1j20u00mhgn2.jpg"
        var url2 = "http://mat1.gtimg.com/www/images/qq2012/qqLogoFilter.png"
        var lists = ImgResUtils.ImageUrl.imageList()
        startCoroutine(DownloadContext(if (mCount < lists.size) lists.get(mCount) else url)) {
            log("协程开始")
            try {
                val imageData = startExpensiveOperations {
                    startLoadImg(this[DownloadContext]!!.url)
                }
                log("拿到图片")
                Glide.with(this).load(if (mCount < lists.size) lists.get(mCount) else url).apply(RequestOptions().circleCrop()).into(iv_pic)
            } catch (timeout: SocketTimeoutException) {
                timeout.printStackTrace()
                Tools.toast("访问超时 ！！！")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        log("协程之后")
    }

    private fun getData() {
        var urls = "https://wd9674052776zkawmw.wilddogio.com/.json"

        startCoroutine(DownloadContext(urls)) {
            try {
                val imageData = startExpensiveOperations {
                    getDatas<CommonBean>(this[DownloadContext]!!.url)
                }
                tv_.setText("data=${imageData?.datas?.date}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun setListener() {
        tv.setOnClickListener(this)
        tv_.setOnClickListener(this)
        iv_pic.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSetContentView(R.layout.activity_main)
    }
}
