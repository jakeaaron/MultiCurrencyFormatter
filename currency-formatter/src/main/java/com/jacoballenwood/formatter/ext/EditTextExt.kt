package com.jacoballenwood.formatter.ext

import android.util.TypedValue
import android.widget.EditText

fun EditText.fitText(minTextSize: Int = 12) {
    val editWidth = width
    if (editWidth > 0) {
        var initialTextSize: Float? = getTag(id) as? Float
        if (initialTextSize == null) {
            initialTextSize = textSize
            setTag(id, initialTextSize)
        }
        var textWidth = paint.measureText(text, 0, text.length)
        var fontSize = textSize
        if (textWidth > editWidth) {
            while (textWidth > editWidth && fontSize > minTextSize) {
                fontSize--
                setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                textWidth = paint.measureText(text, 0, text.length)
            }
        } else {
            while (textWidth < editWidth && fontSize < initialTextSize) {
                fontSize++
                setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                textWidth = paint.measureText(text, 0, text.length)
            }
        }
    }
}