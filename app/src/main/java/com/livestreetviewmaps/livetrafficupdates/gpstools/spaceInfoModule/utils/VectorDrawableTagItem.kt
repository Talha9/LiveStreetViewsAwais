package com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import com.magicgoop.tagsphere.item.TagItem
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class VectorDrawableTagItem(private val drawable: Drawable) : TagItem()  {
    init {
        drawable.bounds = Rect(0, 0, 600, 600)
    }

    override fun drawSelf(
        x: Float,
        y: Float,
        canvas: Canvas,
        paint: TextPaint,
        easingFunction: ((t: Float) -> Float)?
    ) {
        canvas.save()
        canvas.translate(x - 600 / 2f, y - 600 / 2f)
        val alpha = easingFunction?.let { calc ->
            val ease = calc(getEasingValue())
            if (!ease.isNaN()) max(0, min(255, (255 * ease).roundToInt())) else 0
        } ?: 255
        drawable.alpha = alpha
        drawable.draw(canvas)
        canvas.restore()
    }
}