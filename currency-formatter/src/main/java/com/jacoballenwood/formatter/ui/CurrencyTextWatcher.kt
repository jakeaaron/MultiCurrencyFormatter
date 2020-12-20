package com.jacoballenwood.formatter.ui

import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.widget.EditText
import com.jacoballenwood.formatter.ext.withSuperscript
import com.jacoballenwood.formatter.util.ICurrencyFormatter
import com.jacoballenwood.formatter.util.StringUtil.indexOfLastDigit
import java.lang.ref.WeakReference
import java.math.BigDecimal

open class CurrencyTextWatcher(editText: EditText, currencyFormatter: ICurrencyFormatter) :
    AutoSizingTextWatcher(editText),
    ICurrencyTextWatcher {

    private var _editText = WeakReference(editText)
    private val requireEditText: EditText
        get() = _editText.get()!!
    override val editText: EditText?
        get() = _editText.get()

    private var indexOfDecimalPoint = -1
    private var currentIndexOfCents = -1
    private var isDeleting = false

    private var useDecimals = false
    private var currency: BigDecimal = BigDecimal.ZERO
    override val numberValue: BigDecimal
        get() = currency

    override var formatter: ICurrencyFormatter = currencyFormatter
        set(value) {
            field = value
            updateHint()
            amount = value.format(numberValue, useDecimals)
        }

    override var amount: String = editText.text.toString()
        get() = requireEditText.text.toString()
        set(value) {
            field = value
            useDecimals =
                value.contains(formatter.underlyingDecimalFormat.decimalFormatSymbols.decimalSeparator)
            val formatted = formatter.run {
                this@CurrencyTextWatcher.currency = parse(value)
                format(value, useDecimals).run {
                    if (withSymbolSuperscript)
                        withSymbolSuperscript()
                    else
                        this
                }
            }
            if (numberValue > BigDecimal.ZERO)
                requireEditText.setText(formatted)
            else
                requireEditText.text?.clear()
        }

    override var withSymbolSuperscript = true
        set(value) {
            field = value
            updateHint()
            amount = requireEditText.text.toString()
        }
    override var withAutoResize = true

    init {
        initialize()
    }

    private fun initialize() {
        requireEditText.addTextChangedListener(this)
    }

    private fun updateSelection() {
        val sel = if (currentIndexOfCents > -1) {
            (indexOfDecimalPoint + currentIndexOfCents) + if (isDeleting) 0 else 1
        } else
            indexOfLastDigit(requireEditText.text?.toString()) + 1
        if (sel <= requireEditText.length())
            requireEditText.setSelection(sel)
        else
            requireEditText.setSelection(requireEditText.length())
    }

    override fun afterTextChanged(s: Editable?) {
        requireEditText.removeTextChangedListener(this)
        amount = requireEditText.text.toString()
        updateSelection()
        // TODO: replace autosize functionality with composition, rather than inheritance
        if (withAutoResize)
            super.afterTextChanged(s)
        requireEditText.addTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (withAutoResize)
            super.onTextChanged(s, start, before, count)
        isDeleting = count < 1
        indexOfDecimalPoint =
            (s?.indexOf(formatter.underlyingDecimalFormat.decimalFormatSymbols.decimalSeparator)
                ?: 0)
        val wasUsingDecimals = useDecimals
        useDecimals = (indexOfDecimalPoint in 1..start)
        val goingFromDecimalToRounded = isDeleting && wasUsingDecimals && !useDecimals
        if (goingFromDecimalToRounded)
            requireEditText.setText(s?.removeRange(start, s.length))
        currentIndexOfCents = if (useDecimals) {
            start - indexOfDecimalPoint
        } else {
            -1
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (withAutoResize)
            super.beforeTextChanged(s, start, count, after)
    }

    override fun destroy() {
        _editText.get()?.removeTextChangedListener(this)
        _editText.clear()
    }

    private fun updateHint() {
        requireEditText.hint = formatter.run {
            format(BigDecimal.ZERO, false).run {
                if (withSymbolSuperscript)
                    withSymbolSuperscript()
                else
                    this
            }
        }
    }

    private fun String.withSymbolSuperscript(): Spannable =
        this.withSuperscript(
            this.indexOf(formatter.symbol.first()),
            this.indexOf(formatter.symbol.last()) + 1
        )

}

interface ICurrencyTextWatcher : TextWatcher {
    val editText: EditText?
    val numberValue: BigDecimal
    var amount: String
    var withSymbolSuperscript: Boolean
    var withAutoResize: Boolean
    var formatter: ICurrencyFormatter
    fun destroy()
}