package com.dysen.kotlin_demo.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dysen.common_library.adapter.viewpager.BasePagerAdapter
import com.dysen.common_library.utils.ImgResUtils
import com.dysen.common_library.utils.Tools
import com.dysen.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_full_screen.*



class FullScreenActivity : BaseAty(), ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
        getViews(p0 + mIndex)
    }

    var mIndex = 0
    var views = ArrayList<View>()
    var adapter = BasePagerAdapter.ViewAdapter()

    override fun setListener() {
        vp_img.setOnPageChangeListener(this)
    }

    override fun initView() {
        super.initView()
        mIndex = intent.getBundleExtra("data")?.getInt("index")!!
        vp_img?.adapter = adapter
        getViews(mIndex)
    }

    private fun getViews(index: Int) {
        views.clear()
        var lists = ImgResUtils.ImageUrl.imageList()
        tv_index?.text = Tools.getString(R.string.selected_index, index, lists?.size)
        for (url: String in lists) {
            var contentView = LayoutInflater.from(this).inflate(R.layout.layout_img_preview_item, null)
            Glide.with(this).load(url).apply(RequestOptions().circleCrop()).into(contentView.findViewById<ImageView>(R.id.iv_pic))
            views.add(contentView)
        }
//        adapter.setDatas(views)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSetContentView(R.layout.activity_full_screen)
        initView()
    }
}

