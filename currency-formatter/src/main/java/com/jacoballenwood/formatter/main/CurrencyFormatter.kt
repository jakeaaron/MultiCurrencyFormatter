package com.jacoballenwood.formatter.main

import com.jacoballenwood.formatter.main.impl.CurrencyFormatterImpl
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

private var instance: CurrencyFormatterImpl? = null

interface CurrencyFormatter : CurrencyAttrs {
    val underlyingDecimalFormat: DecimalFormat
    fun parse(currency: String): BigDecimal
    fun format(currency: String, decimals: Boolean): String
    fun format(currency: BigDecimal, decimals: Boolean): String

    companion object {

        /**
         * Returns a [CurrencyFormatterImpl] for the specified locale. This is equivalent to
         * <blockquote>
         * <code>getInstance(Currency.getInstance(locale), Currency.getInstance(locale).symbol, locale)</code>
         * </blockquote>
         *
         * @param locale the locale used for the underlying [NumberFormat] and [Currency] instances. defaults to Locale.getDefault
         *
         * @return [CurrencyFormatterImpl] for the specified locale
         *
         * @see Locale
         * @see NumberFormat.getCurrencyInstance(Locale inLocale)
         *
         */
        fun getInstance(locale: Locale = Locale.getDefault()): CurrencyFormatterImpl {
            val currency = try {
                Currency.getInstance(locale)
            } catch (ignore: Exception) {
                Currency.getInstance(Locale("en", "US"))
            }
            return getInstance(currency, currency.symbol, locale)
        }

        /**
         * Returns a [CurrencyFormatterImpl] for the specified currency, symbol, and locale
         *
         * @param currency the currency used by the underlying [NumberFormat] for formatting values
         * @param symbol the currency symbol used for formatting the currency values
         * @param locale the locale used for the underlying [NumberFormat] and [Currency] instances. defaults to Locale.getDefault
         *
         * @return [CurrencyFormatterImpl] for the specified locale
         *
         * @see Currency
         * @see Locale
         * @see NumberFormat.getCurrencyInstance(Locale inLocale)
         *
         */
        fun getInstance(currency: Currency, symbol: String, locale: Locale): CurrencyFormatterImpl {
            if (instance?.currency != currency || instance?.symbol != symbol || instance?.locale != locale)
                instance = CurrencyFormatterImpl(currency, symbol, locale)
            return instance!!
        }

    }
}