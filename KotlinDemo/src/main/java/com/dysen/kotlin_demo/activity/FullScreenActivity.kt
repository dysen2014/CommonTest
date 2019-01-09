package com.dysen.kotlin_demo.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dysen.common_library.adapter.viewpager.BasePagerAdapter
import com.dysen.common_library.utils.ImgResUtils
import com.dysen.common_library.utils.Tools
import com.dysen.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_full_screen.*
import java.lang.reflect.Array.setInt
import java.lang.reflect.AccessibleObject.setAccessible



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
        adapter.setDatas(views)

//        setDefaultItem(index)
//            Glide.with(this).load(lists.get(index % lists.size)).into(contentView.findViewById<ImageView>(R.id.iv_pic))
        //        vp_img.currentItem = mIndex
    }

    private fun setDefaultItem(position: Int) {
        //我这里mViewpager是viewpager子类的实例。如果你是viewpager的实例，也可以这么干。
        try {
            val c = Class.forName("android.support.v4.view.ViewPager")
            val field = c.getDeclaredField("mCurItem")
            field.isAccessible = true
            field.setInt(vp_img, position)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        adapter.notifyDataSetChanged()

        vp_img.setCurrentItem(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSetContentView(R.layout.activity_full_screen)
        initView()
    }
}

