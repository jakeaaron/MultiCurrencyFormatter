package com.jacoballenwood.formatter

import com.jacoballenwood.formatter.util.CurrencyFormatter
import java.util.Currency
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyFormatterTest {

    private fun findLocale(languageTag: String, country: String): Locale {
        for (locale in Locale.getAvailableLocales()) {
            if (locale.language == languageTag && locale.country == country) {
                return locale
            }
        }
        throw NullPointerException()
    }

    @Test
    fun testEnglish() {
        val locale = findLocale("en", "US")
        val currency = Currency.getInstance(locale)
        val symbol = "$"
        val formatter = CurrencyFormatter.getInstance(currency, symbol, locale)

        var formatted = formatter.format("USD$2,000.45", true)
        assertEquals("decimal format was incorrect", "${symbol}2,000.45", formatted)

        formatted = formatter.format("USD$2,0009.45", true)
        assertEquals("decimal format was incorrect", "${symbol}20,009.45", formatted)

        formatted = formatter.format(20.00, true)
        assertEquals("decimal format was incorrect", "${symbol}20.00", formatted)

        formatted = formatter.format(20.00, false)
        assertEquals("whole number format was incorrect", "${symbol}20", formatted)
    }

    @Test
    fun testPortuguese() {
        val locale = findLocale("pt", "BR")
        val currency = Currency.getInstance(locale)
        val symbol = "R$"
        val formatter = CurrencyFormatter.getInstance(currency, symbol, locale)

        var formatted = formatter.format(1234.56, true)
        assertEquals("decimal format was incorrect", "$symbol${NON_BREAKING_SPACE}1.234,56", formatted)

        formatted = formatter.format(20.00, false)
        assertEquals("whole number format was incorrect", "$symbol${NON_BREAKING_SPACE}20", formatted)

        formatted = formatter.format("rr 12.444,88", true)
        assertEquals("whole number format was incorrect", "$symbol${NON_BREAKING_SPACE}12.444,88", formatted)
    }

    @Test
    fun testChinese() {
        val locale = findLocale("zh", "TW")
        val currency = Currency.getInstance(locale)
        val symbol = "¥"
        val formatter = CurrencyFormatter.getInstance(currency, symbol, locale)

        var formatted = formatter.format(1234.56, true)
        assertEquals("decimal format was incorrect", "${symbol}1,234.56", formatted)

        formatted = formatter.format(20.00, false)
        assertEquals("whole number format was incorrect", "${symbol}20", formatted)
    }

    companion object {
        private const val NON_BREAKING_SPACE = ' '
    }
}