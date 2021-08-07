package com.jacoballenwood.formatter.ext

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import com.jacoballenwood.formatter.util.SuperscriptSpanAdjuster

fun String.withSuperscript(startIdx: Int, endIdx: Int): Spannable = SpannableString(this).apply {
    setSpan(
        SuperscriptSpanAdjuster(0.4f),
        startIdx,
        endIdx,
        0
    )
    setSpan(
        RelativeSizeSpan(0.5f),
        startIdx,
        endIdx,
        0
    )
}

fun String.indexOfLastDigit(): Int {
    var result = 0
    for (i in this.indices) {
        if (Character.isDigit(this[i])) {
            result = i
        }
    }
    return result
}