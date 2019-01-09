package com.dysen.kotlin_demo.activity

import android.content.Intent
import android.os.Bundle
import com.dysen.common_library.base.BaseActivity

/**
 *  @package com.dysen.kotlin_demo.activity
 *  @email  dy.sen@qq.com
 *  created by dysen on 2019/1/9 - 3:53 PM
 *  @info
 */
open class BaseAty : BaseActivity(){
    override fun setListener() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun transAty(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    fun transAty(cls: Class<*>, bundle : Bundle) {
        val intent = Intent(this, cls)
        intent.putExtra("data", bundle)
        startActivity(intent)
    }
}