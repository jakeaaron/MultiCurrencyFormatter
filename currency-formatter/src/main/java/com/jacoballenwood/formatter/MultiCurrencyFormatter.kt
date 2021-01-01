package com.jacoballenwood.formatter

import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.jacoballenwood.formatter.main.CurrencyAttrs
import com.jacoballenwood.formatter.main.CurrencyFormatter
import com.jacoballenwood.formatter.main.CurrencyTextWatcher
import com.jacoballenwood.formatter.main.TextChangeListener
import com.jacoballenwood.formatter.main.impl.CurrencyFormatterImpl
import com.jacoballenwood.formatter.main.impl.CurrencyTextWatcherImpl
import java.math.BigDecimal
import java.util.*

class MultiCurrencyFormatter private constructor(
    lifecycleOwner: LifecycleOwner,
    val currencyTextWatcher: CurrencyTextWatcher
) : CurrencyAttrs by currencyTextWatcher.formatter, TextChangeListener by currencyTextWatcher {

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

    /**
     * Sets the locale used by the [CurrencyFormatterImpl] for parsing and formatting currency values
     *
     * @param locale the [Locale] that [CurrencyFormatterImpl] should use to parse and format currency values
     *
     * @return the current [MultiCurrencyFormatter] (useful for chaining different method calls together)
     *
     * @see CurrencyFormatterImpl
     * @see Locale
     */
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

    /**
     * Sets the symbol used by the [CurrencyFormatterImpl] for formatting currency values
     *
     * By default, the symbol will come from the [Currency] value associated with this [MultiCurrencyFormatter]
     *
     * @param symbol the symbol that [CurrencyFormatterImpl] should use to format currency values
     *
     * @return the current [MultiCurrencyFormatter] (useful for chaining different method calls together)
     *
     * @see CurrencyFormatterImpl
     */
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

    /**
     * Sets the currency used by the [CurrencyFormatterImpl] for formatting currency values
     *
     * By default, the currency will come from the [Locale] value associated with this [MultiCurrencyFormatter]
     *
     * @param currency the [Currency] that [CurrencyFormatterImpl] should use to parse and format currency values
     *
     * @return the current [MultiCurrencyFormatter] (useful for chaining different method calls together)
     *
     * @see CurrencyFormatterImpl
     * @see Currency
     */
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

    /**
     * Sets the [CurrencyFormatterImpl] associated with this [MultiCurrencyFormatter]
     *
     * @param currencyFormatter the formatter to parse and format text with
     *
     * @return the current [MultiCurrencyFormatter] (useful for chaining different method calls together)
     *
     * @see CurrencyFormatterImpl
     */
    fun setCurrencyFormatter(currencyFormatter: CurrencyFormatter): MultiCurrencyFormatter {
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
            currencyFormatter: CurrencyFormatter? = CurrencyFormatter.getInstance()
        ): MultiCurrencyFormatter =
            MultiCurrencyFormatter(lifecycleOwner, CurrencyTextWatcherImpl(
                editText,
                currencyFormatter!!
            ).apply {
                formatter = currencyFormatter
            })
    }

}