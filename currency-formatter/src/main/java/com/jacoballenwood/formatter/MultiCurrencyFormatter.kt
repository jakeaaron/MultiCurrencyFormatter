package com.jacoballenwood.formatter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jacoballenwood.formatter.ui.CurrencyTextWatcher
import com.jacoballenwood.formatter.ui.ICurrencyTextWatcher
import java.util.*

class MultiCurrencyFormatter private constructor(
    fragmentActivity: FragmentActivity,
    var currencyTextWatcher: CurrencyTextWatcher,
    var currencyFormatter: CurrencyFormatter
) {

    val textValue: String?
        get() = currencyTextWatcher.amount
    val numberValue: Double
        get() = currencyTextWatcher.currencyVal

    private var lifecycleObserver: LifecycleObserver? = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun destroy() {
            currencyTextWatcher.destroy()
            fragmentActivity.lifecycle.removeObserver(
                this
            )
            lifecycleObserver = null
        }
    }

    init {
        fragmentActivity.lifecycle.addObserver(lifecycleObserver!!)
    }

    fun setLocale(locale: Locale): MultiCurrencyFormatter {
        setCurrencyFormatter(CurrencyFormatter.getInstance(locale))
        return this
    }

    fun setSymbol(symbol: String): MultiCurrencyFormatter {
        setCurrencyFormatter(
            CurrencyFormatter.getInstance(
                currencyFormatter.currency,
                symbol,
                currencyFormatter.locale
            )
        )
        return this
    }

    fun setCurrency(currency: Currency): MultiCurrencyFormatter {
        setCurrencyFormatter(
            CurrencyFormatter.getInstance(
                currency,
                currencyFormatter.symbol,
                currencyFormatter.locale
            )
        )
        return this
    }

    fun setCurrencyFormatter(currencyFormatter: CurrencyFormatter): MultiCurrencyFormatter {
        this.currencyFormatter = currencyFormatter
        currencyTextWatcher.setFormatter(currencyFormatter)
        return this
    }

    fun setAutoResize(enabled: Boolean): MultiCurrencyFormatter {
        currencyTextWatcher.withAutoResize = enabled
        return this
    }

    fun setSuperscript(enabled: Boolean): MultiCurrencyFormatter {
        currencyTextWatcher.withSuperScript = enabled
        return this
    }

    fun setAmount(amount: String) {
        currencyTextWatcher.setAmount(amount)
    }

    companion object {

        fun newInstance(
            fragmentActivity: FragmentActivity,
            editText: EditText,
            currencyTextWatcher: CurrencyTextWatcher? = CurrencyTextWatcher(editText),
            currencyFormatter: CurrencyFormatter? = CurrencyFormatter.getInstance()
        ): MultiCurrencyFormatter =
            MultiCurrencyFormatter(fragmentActivity, currencyTextWatcher!!, currencyFormatter!!)

    }

}