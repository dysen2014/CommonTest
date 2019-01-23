package com.dysen.kotlin_demo.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dysen.common_library.utils.DateUtils
import com.dysen.common_library.utils.ImgResUtils
import com.dysen.kotlin_demo.CommonBean
import com.dysen.kotlin_demo.R
import com.dysen.kotlin_demo.async.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @package com.dysen.kotlin_demo
 * @email dy.sen@qq.com
 * created by dysen on 2019/1/8 - 10:31 AM
 * @info
 */
class MainActivity : BaseAty(), View.OnClickListener {

    var mCount: Int = 0
    var url = "https://wd9674052776zkawmw.wilddogio.com/.json"

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_img -> {
                var lists = ImgResUtils.ImageUrl.imageList()
                Glide.with(this).load(lists.get(mCount%lists.size)).apply(RequestOptions().circleCrop()).into(iv_pic)
                mCount++
            }
            R.id.tv_get -> {
                getData(tv_get)
            }
            R.id.tv_post -> {
                getData(tv_post)
            }
            R.id.iv_pic -> {
                var bundle = Bundle()
                bundle.putInt("index", mCount-1)
                transAty(FullScreenActivity::class.java, bundle)
            }
        }
    }

    override fun initView() {
        super.initView()
        tvTitle.setText(R.string.app_name)

    }

    private fun getData(view:TextView) {

        startCoroutine(DownloadContext(url)) {
            try {
                val imageData = startExpensiveOperations {
                    getDatas<CommonBean>(this[DownloadContext]!!.url)
                }
                view.setText("data=${imageData?.datas?.date}------${DateUtils.getCurrentTime2()}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun setListener() {
        tv_img.setOnClickListener(this)
        tv_get.setOnClickListener(this)
        tv_post.setOnClickListener(this)
        iv_pic.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSetContentView(R.layout.activity_main)
    }
}
