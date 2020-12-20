package com.jacoballenwood.formatter

import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry
import com.jacoballenwood.formatter.ui.CurrencyTextWatcher
import com.jacoballenwood.formatter.util.CurrencyFormatter
import java.util.Currency
import java.util.Locale
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyTextWatcherTest {

    @Test
    fun testWatcher() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val usLocale = Locale("en", "US")
        val currency = Currency.getInstance(usLocale)
        val symbol = "$"
        val currencyFormatter =
            CurrencyFormatter.getInstance(locale = usLocale, currency = currency, symbol = symbol)
        val editText = EditText(context)
        CurrencyTextWatcher(editText, currencyFormatter).apply {
            editText.addTextChangedListener(this)
        }

        editText.setText("25.00")
        assertEquals("format was incorrect", editText.text.toString(), "$25.00")

        editText.setText("5")
        assertEquals("format was incorrect", editText.text.toString(), "$5")

        editText.setText("0.05")
        assertEquals("format was incorrect", editText.text.toString(), "$0.05")
    }
}
