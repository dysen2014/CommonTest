package com.dysen.svga.drawer

import android.graphics.Canvas
import android.widget.ImageView
import com.dysen.svga.SVGAVideoEntity
import com.dysen.svga.entities.SVGAVideoSpriteFrameEntity
import com.dysen.svga.utils.SVGAScaleInfo

/**
 * Created by cuiminghui on 2017/3/29.
 */

open internal class SGVADrawer(val videoItem: SVGAVideoEntity) {

    val scaleInfo = SVGAScaleInfo()

    inner class SVGADrawerSprite(val imageKey: String?, val frameEntity: SVGAVideoSpriteFrameEntity)

    internal fun requestFrameSprites(frameIndex: Int): List<SVGADrawerSprite> {
        return videoItem.sprites.mapNotNull {
            if (frameIndex >= 0 && frameIndex < it.frames.size) {
                if (it.frames[frameIndex].alpha <= 0.0) {
                    return@mapNotNull null
                }
                return@mapNotNull SVGADrawerSprite(it.imageKey, it.frames[frameIndex])
            }
            return@mapNotNull null
        }
    }

    open fun drawFrame(canvas : Canvas, frameIndex: Int, scaleType: ImageView.ScaleType) {
        scaleInfo.performScaleType(canvas.width.toFloat(),canvas.height.toFloat(), videoItem.videoSize.width.toFloat(), videoItem.videoSize.height.toFloat(), scaleType)
    }

}
