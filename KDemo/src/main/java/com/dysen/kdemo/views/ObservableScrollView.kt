package com.dysen.kdemo.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import java.util.*

class ObservableScrollView : ScrollView {
    private var scrollListener: ScrollListener? = null
    var list_stickview = ArrayList<View>()
    private set
    var list_spaceview = ArrayList<View>()
    private set

    private var left_ls=0
    private var top_ls=0
    private var oldl_ls=0
    private var oldt_ls=0
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        left_ls=l
        top_ls=t
        oldl_ls=oldl
        oldt_ls=oldt
        onObservableScroll(left_ls, top_ls, oldl_ls, oldt_ls)
        //scrollListener?.onScroll(l, t, oldl, oldt)
    }

    fun registerListener(scrollListener: ScrollListener) {
        this.scrollListener = scrollListener
    }

    fun add_stickview(vararg v:View?){
        for(vt in v){
            vt?.let { list_stickview.add(it) }
        }
    }
    fun add_spaceview(vararg v:View?){
        for(vt in v){
            vt?.let { list_spaceview.add(it) }
        }
    }
    fun getStickSize():Int{
        return list_stickview.size
    }
    fun addOnGlobalLayoutListener(){
        list_stickview.forEach {
            if(it.viewTreeObserver.isAlive){
                it.viewTreeObserver.addOnGlobalLayoutListener {
                    //scrollListener?.onScroll(left_ls, top_ls, oldl_ls, oldt_ls)
                    onObservableScroll(left_ls, top_ls, oldl_ls, oldt_ls)
                }
            }
        }

    }
    fun onObservableScroll(left: Int, top: Int,oldl: Int, oldt: Int){
        if(left>0||oldl>0||oldt>0){

        }
        var stickview_size:Int=getStickSize()
        for(n in 1..(stickview_size)){
            var stickview:View= list_stickview[n-1]
            var spaceview:View= list_spaceview[n-1]
            var spaceview2:View?=null
            var is_move=false
            if(n<stickview_size){
                spaceview2= list_spaceview[n]
                if(spaceview2.top<=(top+stickview.height)){
                    is_move=true
                }
            }
            if(is_move){
                stickview.translationY=(top+(spaceview2!!.top-(top+stickview.height))).toFloat()
            }else{
                stickview.translationY=Math.max(top, spaceview.top).toFloat()
            }

        }
    }
    //设置高度
    fun setSpaceHight(){
        var stickview_size:Int=getStickSize()
        for(n in 1..(stickview_size)){
            var stickview:View= list_stickview[n-1]
            var spaceview:View= list_spaceview[n-1]
            val params1 = spaceview.layoutParams as LinearLayout.LayoutParams
            //获取当前控件的布局对象
            params1.height =stickview.height//设置当前控件布局的高度
            spaceview.layoutParams = params1//将设置好的布局参数应用到控件中 getMeasureHeight
        }
    }
    interface ScrollListener {
        fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int)
    }
}
