package com.dysen.common_library.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.design.widget.CollapsingToolbarLayout
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.dysen.common_library.base.BaseActivity
import kotlinx.android.synthetic.main.common_lay_title2.*

/**
 *  @package com.dysen.common_library.utils
 *  @email  dy.sen@qq.com
 *  created by dysen on 2019/2/10 - 4:32 PM
 *  @info
 */
open class ToolStatus : BaseActivity(){
    override fun setListener() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun fullScreen(activity: Activity) {
        //设置系统UI参数
        //setSystemUiVisibility(int visibility)传入的实参类型如下：
        //1.View.SYSTEM_UI_FLAG_VISIBLE ：状态栏和Activity共存，Activity不全屏显示。也就是应用平常的显示画面
        //2.View.SYSTEM_UI_FLAG_FULLSCREEN ：Activity全屏显示，且状态栏被覆盖掉
        //3. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ：Activity全屏显示，但是状态栏不会被覆盖掉，而是正常显示，只是Activity顶端布   局会被覆盖住
        //4.View.INVISIBLE ： Activity全屏显示，隐藏状态栏
        //5.View.SYSTEM_UI_FLAG_LAYOUT_STABLE  ： 稳定布局
        //6.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ：改变状态栏字体颜色 (android 6.0以上有效)
        val window: Window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView: View = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        decorView.systemUiVisibility = option
        //给系统状态栏着色：
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //给系统状态栏设置透明颜色：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }else{
            return
        }

        //获取Toolbar控件，获取状态栏高度，给Toolbar控件的MarginTop设置状态栏的高度
        //Toolbar父控件为CollapsingToolbarLayout
        val layoutParams: CollapsingToolbarLayout.LayoutParams = coordinator_toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0)
        coordinator_toolbar.layoutParams = layoutParams
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "  ")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}