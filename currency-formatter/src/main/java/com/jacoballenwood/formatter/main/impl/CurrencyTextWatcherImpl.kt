package com.jacoballenwood.formatter.main.impl

import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.widget.EditText
import com.jacoballenwood.formatter.ext.fitText
import com.jacoballenwood.formatter.ext.indexOfLastDigit
import com.jacoballenwood.formatter.ext.withSuperscript
import com.jacoballenwood.formatter.main.*
import java.lang.ref.WeakReference
import java.math.BigDecimal

class CurrencyTextWatcherImpl internal constructor(
    editText: EditText,
    currencyFormatter: CurrencyFormatter,
    listener: Listeners<TextWatcher>
) : CurrencyTextWatcher, Listeners<TextWatcher> by listener {

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

    override var formatter: CurrencyFormatter = currencyFormatter
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
                this@CurrencyTextWatcherImpl.currency = parse(value)
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
            (requireEditText.text?.toString()?.indexOfLastDigit() ?: 0) + 1
        if (sel <= requireEditText.length())
            requireEditText.setSelection(sel)
        else
            requireEditText.setSelection(requireEditText.length())
    }

    override fun afterTextChanged(s: Editable?) {
        notify { it.afterTextChanged(s) }
        requireEditText.removeTextChangedListener(this)
        amount = requireEditText.text.toString()
        updateSelection()
        if (withAutoResize)
            requireEditText.fitText()
        requireEditText.addTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        notify { it.onTextChanged(s, start, before, count) }
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
        notify { it.beforeTextChanged(s, start, count, after) }
    }

    override fun destroy() {
        _editText.get()?.removeTextChangedListener(this)
        _editText.clear()
        clear()
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