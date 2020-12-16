package com.example.currencylibrary

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

internal class SuperscriptSpanAdjuster(private val ratio: Float = 0.4f) : MetricAffectingSpan() {

    override fun updateDrawState(tp: TextPaint?) {
        tp?.updateBaseline()
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.updateBaseline()
    }

    private fun TextPaint.updateBaseline() {
        baselineShift += (ascent() * ratio).toInt()
    }
}
