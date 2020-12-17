package com.jacoballenwood.formatter.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.jacoballenwood.formatter.AutoSizingTextWatcher
import com.jacoballenwood.formatter.CurrencyFormatter
import com.jacoballenwood.formatter.util.StringUtil.indexOfLastDigit
import java.lang.ref.WeakReference

open class CurrencyTextWatcher(editText: EditText) : AutoSizingTextWatcher(editText),
    ICurrencyTextWatcher {

    private var _editText = WeakReference(editText)
    val editText: EditText
        get() = _editText.get()!!
    private var formatter = CurrencyFormatter.getInstance()

    private var indexOfDecimalPoint = -1
    private var currentIndexOfCents = -1
    private var isDeleting = false

    private var useDecimals = false
    private var currencyValData: Double = 0.0
    override val currencyVal: Double
        get() = currencyValData
    override val amount: String?
        get() = editText.text?.toString()

    var withSuperScript = true
    var withAutoResize = true

    init {
        initialize()
    }

    private fun initialize() {
        editText.addTextChangedListener(this)
    }

    override fun setFormatter(formatter: CurrencyFormatter) {
        this.formatter = formatter
        editText.hint = formatter.run {
            format(0.0, false).withSuperscript()
        }
        setAmount(formatter.format(currencyVal, useDecimals))
    }

    override fun setAmount(amount: String) {
        useDecimals = amount.contains(formatter.decimalSeparator)
        val formatted = formatter.run {
            currencyValData = parse(amount)
            format(amount, useDecimals).run {
                if (withSuperScript)
                    withSuperscript()
                else
                    this
            }
        }
        if (currencyVal > 0)
            editText.setText(formatted)
        else
            editText.text?.clear()
    }

    private fun updateSelection() {
        val sel = if (currentIndexOfCents > -1) {
            (indexOfDecimalPoint + currentIndexOfCents) + if (isDeleting) 0 else 1
        } else
            indexOfLastDigit(editText.text?.toString()) + 1
        if (sel <= editText.length())
            editText.setSelection(sel)
        else
            editText.setSelection(editText.length())
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)
        setAmount(editText.text.toString())
        updateSelection()
        if (withAutoResize)
            super.afterTextChanged(s)
        editText.addTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (withAutoResize)
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

    override fun destroy() {
        _editText.get()?.removeTextChangedListener(this)
        _editText.clear()
    }
}

interface ICurrencyTextWatcher : TextWatcher {
    val currencyVal: Double
    val amount: String?
    fun setFormatter(formatter: CurrencyFormatter)
    fun setAmount(amount: String)
    fun destroy()
}