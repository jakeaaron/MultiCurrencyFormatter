package com.jacoballenwood.formatter.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * CurrencyFormatter parses and formats currency values based on the currency, symbol, and locale parameters
 *
 * @param currency the currency that the underlying NumberFormat will use to format and parse currency values
 * @param symbol the currency symbol to use for formatting the currency string
 * @param locale the locale that the underlying NumberFormat will use to format and parse currency values
 *
 * @see NumberFormat.getCurrencyInstance
 * @see NumberFormat.setCurrency
 * @see java.text.DecimalFormatSymbols
 */
class CurrencyFormatter private constructor(
    override val currency: Currency,
    override val symbol: String,
    override val locale: Locale
) : ICurrencyFormatter, CurrencyAttrs {

    private val currencyFormatter: DecimalFormat =
        NumberFormat.getCurrencyInstance(locale) as DecimalFormat
    private val roundedCurrencyFormatter: DecimalFormat
    private val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(locale)
    override val underlyingDecimalFormat: DecimalFormat
        get() = currencyFormatter

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

    /**
     * Parses the given text to produce a BigDecimal Number
     *
     * If the text cannot be parsed properly, a value of BigDecimal.ZERO will be returned
     *
     * The BigDecimal will be scaled to include only the number of fractional digits allowed by NumberFormat.maximumFractionDigits
     *
     * @param currency the currency value to be parsed
     *
     * @return a BigDecimal parsed from the string
     *
     * @see NumberFormat.getMaximumFractionDigits
     * @see NumberFormat.parse
     * @see BigDecimal
     */
    override fun parse(currency: String): BigDecimal {
        val javaCurrency = currencyFormatter.currency
        val str = currency.replace(javaCurrency?.symbol ?: "", "")
            .replace(javaCurrency?.currencyCode ?: "", "")
            .replace(symbol, "")
            .replace(cleaningRegex, "")
        return (try {
            BigDecimal(currencyFormatter.parse(str)?.toString())
        } catch (e: Exception) {
            try {
                BigDecimal(numberFormatter.parse(str)?.toString())
            } catch (e2: Exception) {
                BigDecimal(str.takeIf { it.isNotEmpty() } ?: "0")
            }
        }).setScale(currencyFormatter.maximumFractionDigits, BigDecimal.ROUND_DOWN)
    }

    /**
     *
     */
    override fun format(currency: String, decimals: Boolean): String {
        return format(parse(currency), decimals)
    }

    /**
     *
     */
    override fun format(currency: BigDecimal, decimals: Boolean): String = if (!decimals) {
        roundedCurrencyFormatter.format(currency.toInt())
    } else
        currencyFormatter.format(currency)

    companion object {

        private val cleaningRegex = "[^\\d+.,]".toRegex()

        private var instance: CurrencyFormatter? = null

        /**
         *
         */
        fun getInstance(locale: Locale = Locale.getDefault()): CurrencyFormatter {
            val currency = try {
                Currency.getInstance(locale)
            } catch (ignore: Exception) {
                Currency.getInstance(Locale("en", "US"))
            }
            return getInstance(currency, currency.symbol, locale)
        }

        /**
         *
         */
        fun getInstance(currency: Currency, symbol: String, locale: Locale): CurrencyFormatter {
            if (instance?.currency != currency || instance?.symbol != symbol || instance?.locale != locale)
                instance = CurrencyFormatter(currency, symbol, locale)
            return instance!!
        }
    }
}

interface ICurrencyFormatter : CurrencyAttrs {
    val underlyingDecimalFormat: DecimalFormat
    fun parse(currency: String): BigDecimal
    fun format(currency: String, decimals: Boolean): String
    fun format(currency: BigDecimal, decimals: Boolean): String
}

interface CurrencyAttrs {
    val currency: Currency
    val symbol: String
    val locale: Locale
}