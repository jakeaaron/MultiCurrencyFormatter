package com.example.currencylibrary

import android.text.Editable
import android.widget.EditText
import java.lang.ref.WeakReference

open class CurrencyTextWatcher(editText: EditText) : AutoSizingTextWatcher(editText) {

    private var _editText = WeakReference<EditText>(editText)
    private val editText: EditText
        get() = _editText.get()!!
    private var formatter = CurrencyFormatter.getInstance()

    private var indexOfDecimalPoint = -1
    private var currentIndexOfCents = -1
    private var isDeleting = false

    private var useDecimals = false
    var currencyVal: Double = 0.0
        private set
    val amount: String?
        get() = editText.text?.toString()

    fun setFormatter(formatter: CurrencyFormatter) {
        this.formatter = formatter
        editText.hint = formatter.run {
            format(0.0, false).withSuperscript()
        }
        setAmount(formatter.format(currencyVal, useDecimals))
    }

    fun setAmount(amount: String) {
        useDecimals = amount.contains(formatter.decimalSeparator)
        val formatted = formatter.run {
            currencyVal = parse(amount)
            format(amount, useDecimals).withSuperscript()
        }
        if (currencyVal > 0)
            editText.setText(formatted)
        else
            editText.text?.clear()
    }

    private fun setSelection() {
        val sel = if (currentIndexOfCents > -1) {
            (indexOfDecimalPoint + currentIndexOfCents) + if (isDeleting) 0 else 1
        } else
            indexOfLastDigit(editText.text?.toString()) + 1
        if (sel <= editText.length())
            editText.setSelection(sel)
        else
            editText.setSelection(editText.length())
    }

    private fun indexOfLastDigit(str: String?): Int {
        var result = 0
        str ?: return result
        for (i in str.indices) {
            if (Character.isDigit(str[i])) {
                result = i
            }
        }
        return result
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)
        setAmount(editText.text.toString())
        setSelection()
        super.afterTextChanged(s)
        editText.addTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        super.onTextChanged(s, start, before, count)
        isDeleting = count < 1
        indexOfDecimalPoint = (s?.indexOf(formatter.decimalSeparator) ?: 0)
        val wasUsingDecimals = useDecimals
        useDecimals = (indexOfDecimalPoint in 1..start)
        val goingFromDecimalToRounded = isDeleting && wasUsingDecimals && !useDecimals
        if (goingFromDecimalToRounded)
            editText.setText(s?.removeRange(start, s.length))
        currentIndexOfCents = if (useDecimals) {
            start - indexOfDecimalPoint
        } else {
            -1
        }
    }

    fun cleanup() {
        _editText.clear()
    }
}
