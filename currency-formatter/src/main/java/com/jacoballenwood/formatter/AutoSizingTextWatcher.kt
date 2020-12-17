package com.jacoballenwood.formatter

import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import java.lang.ref.WeakReference

open class AutoSizingTextWatcher(editText: EditText) : TextWatcher {

    private val _editText = WeakReference(editText)
    private val editText: EditText
        get() = _editText.get()!!

    private var initialTextSize = -1f
    private var lastTextCheck: CharSequence = ""
    private var lastEditWidth = 0

    private fun updateTextSize(text: CharSequence) {
        val editWidth = editText.width
        if (editWidth > 0) {
            if (text == lastTextCheck && lastEditWidth == editWidth)
                return
            lastTextCheck = text
            lastEditWidth = editWidth
            if (initialTextSize == -1f)
                initialTextSize = editText.textSize
            var textWidth = editText.paint.measureText(text, 0, text.length)
            var fontSize = editText.textSize
            if (textWidth > editWidth) {
                while (textWidth > editWidth && fontSize > 12) {
                    fontSize--
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                    textWidth = editText.paint.measureText(text, 0, text.length)
                }
            } else {
                while (textWidth < editWidth && fontSize < initialTextSize) {
                    fontSize++
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                    textWidth = editText.paint.measureText(text, 0, text.length)
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        editText.text?.let { updateTextSize(it) }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        updateTextSize(s ?: return)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
