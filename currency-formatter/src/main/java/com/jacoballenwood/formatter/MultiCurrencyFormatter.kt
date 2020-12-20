package com.jacoballenwood.formatter

import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.jacoballenwood.formatter.ui.CurrencyTextWatcher
import com.jacoballenwood.formatter.ui.ICurrencyTextWatcher
import com.jacoballenwood.formatter.util.CurrencyFormatter
import com.jacoballenwood.formatter.util.ICurrencyFormatter
import com.jacoballenwood.formatter.util.IMultiCurrency
import java.math.BigDecimal
import java.util.*

class MultiCurrencyFormatter private constructor(
    lifecycleOwner: LifecycleOwner,
    val currencyTextWatcher: ICurrencyTextWatcher
) : IMultiCurrency by currencyTextWatcher.formatter {

    val textValue: String
        get() = currencyTextWatcher.amount
    val numberValue: BigDecimal
        get() = currencyTextWatcher.numberValue

    private var lifecycleObserver: LifecycleObserver? = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun destroy() {
            currencyTextWatcher.destroy()
            lifecycleOwner.lifecycle.removeObserver(
                this
            )
            lifecycleObserver = null
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver!!)
    }

    fun setLocale(locale: Locale): MultiCurrencyFormatter {
        setCurrencyFormatter(
            CurrencyFormatter.getInstance(
                currency,
                symbol,
                locale
            )
        )
        return this
    }

    fun setSymbol(symbol: String): MultiCurrencyFormatter {
        setCurrencyFormatter(
            CurrencyFormatter.getInstance(
                currency,
                symbol,
                locale
            )
        )
        return this
    }

    fun setCurrency(currency: Currency): MultiCurrencyFormatter {
        setCurrencyFormatter(
            CurrencyFormatter.getInstance(
                currency,
                symbol,
                locale
            )
        )
        return this
    }

    fun setCurrencyFormatter(currencyFormatter: ICurrencyFormatter): MultiCurrencyFormatter {
        currencyTextWatcher.formatter = currencyFormatter
        return this
    }

    fun setAutoResize(enabled: Boolean): MultiCurrencyFormatter {
        currencyTextWatcher.withAutoResize = enabled
        return this
    }

    fun setSymbolSuperscript(enabled: Boolean): MultiCurrencyFormatter {
        currencyTextWatcher.withSymbolSuperscript = enabled
        return this
    }

    fun setAmount(amount: String) {
        currencyTextWatcher.amount = amount
    }

    companion object {

        fun newInstance(
            lifecycleOwner: LifecycleOwner,
            editText: EditText,
            currencyFormatter: ICurrencyFormatter? = CurrencyFormatter.getInstance(),
            currencyTextWatcher: ICurrencyTextWatcher? = CurrencyTextWatcher(
                editText,
                currencyFormatter!!
            )
        ): MultiCurrencyFormatter =
            MultiCurrencyFormatter(lifecycleOwner, currencyTextWatcher!!.apply {
                formatter = currencyFormatter!!
            })

    }

}