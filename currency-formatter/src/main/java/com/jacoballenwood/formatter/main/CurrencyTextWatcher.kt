package com.jacoballenwood.formatter.main

import android.text.TextWatcher
import android.widget.EditText
import java.math.BigDecimal

interface CurrencyTextWatcher : TextWatcher, TextChangeListener {
    val editText: EditText?
    val numberValue: BigDecimal
    var amount: String
    var withSymbolSuperscript: Boolean
    var withAutoResize: Boolean
    var formatter: CurrencyFormatter
    fun destroy()
}