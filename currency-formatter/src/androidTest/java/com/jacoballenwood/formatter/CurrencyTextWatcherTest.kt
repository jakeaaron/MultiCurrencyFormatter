package com.jacoballenwood.formatter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry
import com.jacoballenwood.formatter.main.impl.CurrencyTextWatcherImpl
import com.jacoballenwood.formatter.main.impl.CurrencyFormatterImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.util.Currency
import java.util.Locale
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyTextWatcherTest {

    @Test
    fun watcher_formats_text_correctly() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val usLocale = Locale("en", "US")
        val currency = Currency.getInstance(usLocale)
        val symbol = "$"
        val currencyFormatter =
            CurrencyFormatterImpl.getInstance(locale = usLocale, currency = currency, symbol = symbol)
        val editText = EditText(context)
        CurrencyTextWatcherImpl(editText, currencyFormatter)

        editText.setText("25.00")
        assertEquals("format was incorrect", editText.text.toString(), "$25.00")

        editText.setText("5")
        assertEquals("format was incorrect", editText.text.toString(), "$5")

        editText.setText("0.05")
        assertEquals("format was incorrect", editText.text.toString(), "$0.05")
    }

    @Test
    fun text_listeners_are_notified() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val usLocale = Locale("en", "US")
        val currencyFormatter = CurrencyFormatterImpl.getInstance(locale = usLocale)
        val editText = EditText(context)
        val watcher = CurrencyTextWatcherImpl(editText, currencyFormatter)

        var customListenerNotified = 0
        val customListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                customListenerNotified++
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                customListenerNotified++
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                customListenerNotified++
            }
        }
        watcher.addTextChangeListener(customListener)

        watcher.amount = "50"

        assertTrue(customListenerNotified >= 3)

        val notifiedNum = customListenerNotified

        watcher.removeTextChangeListener(customListener)

        watcher.amount = "23.00"

        assertEquals(notifiedNum, customListenerNotified)
    }

    @Test
    fun destroy_cleans_up_view_and_listeners() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val usLocale = Locale("en", "US")
        val currencyFormatter = CurrencyFormatterImpl.getInstance(locale = usLocale)
        val editText = EditText(context)
        val watcher = CurrencyTextWatcherImpl(editText, currencyFormatter)

        watcher.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        watcher.destroy()

        assertTrue(watcher.editText == null)
        assertTrue(watcher.listeners.isEmpty())
    }
}
