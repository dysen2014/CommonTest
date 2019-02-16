package com.dysen.svga

/**
 * @package com.dysen.svga
 * @email dy.sen@qq.com
 * created by dysen on 2019/2/15 - 14:31 PM
 * @info.
 */
interface SVGACallback {

    fun onPause()
    fun onFinished()
    fun onRepeat()
    fun onStep(frame: Int, percentage: Double)

}