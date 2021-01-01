package com.jacoballenwood.formatter.main

import android.text.TextWatcher
import android.widget.EditText
import com.jacoballenwood.formatter.main.impl.CurrencyTextWatcherImpl
import java.math.BigDecimal

interface CurrencyTextWatcher : TextWatcher, TextChangeListener {
    val editText: EditText?
    val numberValue: BigDecimal
    var amount: String
    var withSymbolSuperscript: Boolean
    var withAutoResize: Boolean
    var formatter: CurrencyFormatter
    fun destroy()

    companion object {
        fun newInstance(
            editText: EditText,
            currencyFormatter: CurrencyFormatter
        ): CurrencyTextWatcher = CurrencyTextWatcherImpl(
            editText,
            currencyFormatter,
            ListenersImpl()
        )
    }
}