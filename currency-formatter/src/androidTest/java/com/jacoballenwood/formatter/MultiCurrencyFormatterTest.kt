package com.jacoballenwood.formatter


import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.jacoballenwood.formatter.ui.CurrencyTextWatcher
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import java.math.BigDecimal

class TestActivity : FragmentActivity()

@RunWith(JUnit4::class)
class MultiCurrencyFormatterTest {

    private fun setUpActivity(): Pair<ActivityScenario<TestActivity>, TestActivity> {
        val scenario = launchActivity<TestActivity>()
        var act: TestActivity? = null
        scenario.onActivity {
            act = it
        }
        return Pair(scenario, act!!)
    }

    private fun setUpFormatter(activity: FragmentActivity): MultiCurrencyFormatter =
        MultiCurrencyFormatter.newInstance(
            activity,
            EditText(activity)
        )

    @Test
    fun setting_and_getting_amount() {
        val (_, activity) = setUpActivity()
        val formatter = setUpFormatter(activity)
        formatter.setAmount("$25.00")
        Assert.assertEquals("$25.00", formatter.textValue)
        formatter.setAmount("$50.00")
        Assert.assertEquals(BigDecimal(50.00), formatter.numberValue)
    }

    @Test
    fun setting_custom_symbol() {
        val (_, activity) = setUpActivity()
        val formatter = setUpFormatter(activity)
        val newSymbol = "💸"
        formatter.setSymbol(newSymbol)
        formatter.setAmount("50.00")
        Assert.assertEquals("${newSymbol}50.00", formatter.textValue)
    }

    @Test
    fun setting_locale() {
        val (_, activity) = setUpActivity()
        val formatter = setUpFormatter(activity)
        val currencyAmount = 27.50
        formatter.setAmount(currencyAmount.toString())
        formatter.setLocale(Locale.US)
        Assert.assertEquals(formatter.currencyTextWatcher.formatter.locale, Locale.US)
        formatter.setLocale(Locale.CHINA)
        Assert.assertEquals(formatter.currencyTextWatcher.formatter.locale, Locale.CHINA)
    }

    @Test
    fun setting_currency() {
        val (_, activity) = setUpActivity()
        val formatter = setUpFormatter(activity)
        val currencyAmount = 21.50
        var currency = Currency.getInstance(Locale.TAIWAN)
        formatter.setAmount(currencyAmount.toString())
        formatter.setLocale(Locale.US)
        formatter.setCurrency(currency)
        Assert.assertEquals(formatter.currencyTextWatcher.formatter.currency, currency)
        currency = Currency.getInstance(Locale.JAPAN)
        formatter.setCurrency(currency)
        Assert.assertEquals(formatter.currencyTextWatcher.formatter.currency, currency)
    }

    @Test
    fun edittext_reference_cleared_on_lifecycle_destroy() {
        val (scene, activity) = setUpActivity()
        val formatter = setUpFormatter(activity)
        Assert.assertNotNull((formatter.currencyTextWatcher as CurrencyTextWatcher).editText)
        scene.moveToState(Lifecycle.State.DESTROYED)
        Assert.assertNull((formatter.currencyTextWatcher as CurrencyTextWatcher).editText)
        scene.close()
    }

}