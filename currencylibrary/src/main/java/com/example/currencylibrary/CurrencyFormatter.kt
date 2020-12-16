package com.example.currencylibrary

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class CurrencyFormatter private constructor(
    internal val currency: Currency,
    internal val symbol: String,
    internal val locale: Locale
) {

    private val currencyFormatter: DecimalFormat = NumberFormat.getCurrencyInstance(locale) as DecimalFormat
    private val roundedCurrencyFormatter: DecimalFormat
    private val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(locale)
    val decimalSeparator: Char = currencyFormatter.decimalFormatSymbols.decimalSeparator

    init {
        val symbols = currencyFormatter.decimalFormatSymbols
        symbols.currencySymbol = symbol
        currencyFormatter.apply {
            this.currency = this@CurrencyFormatter.currency
            this.decimalFormatSymbols = symbols
        }
        roundedCurrencyFormatter = (currencyFormatter.clone() as DecimalFormat).apply {
            this.maximumFractionDigits = 0
        }
    }

    fun parse(currency: String): Double {
        val javaCurrency = currencyFormatter.currency
        val str = currency.replace(javaCurrency?.symbol ?: "", "")
            .replace(javaCurrency?.currencyCode ?: "", "")
            .replace(symbol, "")
            .replace(cleaningRegex, "")
        return try {
            currencyFormatter.parse(str)?.toDouble()!!
        } catch (e: Exception) {
            try {
                numberFormatter.parse(str)?.toDouble()!!
            } catch (e2: Exception) {
                str.toDoubleOrNull() ?: 0.0
            }
        }
    }

    fun format(currency: String, decimals: Boolean): String {
        return format(parse(currency), decimals)
    }

    fun format(currency: Double, decimals: Boolean): String = if (!decimals) {
        roundedCurrencyFormatter.format(currency.toInt())
    } else
        currencyFormatter.format(currency)

    fun String.withSuperscript(): Spannable = SpannableString(this).apply {
        setSpan(
            SuperscriptSpanAdjuster(0.4f),
            this.indexOf(symbol.first()),
            this.indexOf(symbol.last()) + 1,
            0
        )
        setSpan(
            RelativeSizeSpan(0.5f),
            this.indexOf(symbol.first()),
            this.indexOf(symbol.last()) + 1,
            0
        )
    }

    companion object {

        private val cleaningRegex = "[^\\d+.,]".toRegex()

        private var instance: CurrencyFormatter? = null

        fun getInstance(locale: Locale = Locale.getDefault()): CurrencyFormatter {
            val currency = try {
                Currency.getInstance(locale)
            } catch (ignore: Exception) {
                Currency.getInstance(Locale("en", "US"))
            }
            return getInstance(currency, currency.symbol, locale)
        }

        fun getInstance(currency: Currency, symbol: String, locale: Locale): CurrencyFormatter {
            if (instance?.currency != currency || instance?.symbol != symbol || instance?.locale != locale)
                instance = CurrencyFormatter(currency, symbol, locale)
            return instance!!
        }
    }
}
