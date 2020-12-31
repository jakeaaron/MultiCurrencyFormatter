package com.jacoballenwood.currency

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jacoballenwood.formatter.MultiCurrencyFormatter
import com.jacoballenwood.formatter.ui.CurrencyTextWatcher
import com.jacoballenwood.formatter.ui.ICurrencyTextWatcher
import com.jacoballenwood.formatter.util.CurrencyFormatter
import com.jacoballenwood.formatter.util.ICurrencyFormatter
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    private var formatter: MultiCurrencyFormatter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.gift)?.let { editText ->
            formatter = MultiCurrencyFormatter.newInstance(this, editText)
                .setLocale(Locale.US) // set initial locale
                .setSymbol("💸") // set custom symbol
                .setAutoResize(true)
                .setSymbolSuperscript(true)
        }

        findViewById<ChipGroup>(R.id.btnContainer)?.let { container ->
            locales.forEach { locale ->
                container.addView(
                    (LayoutInflater.from(this)
                        .inflate(R.layout.view_locale_chip, container, false) as Chip)
                        .apply {
                            isCheckable = true
                            id = locale.hashCode()
                            tag = locale
                            text = locale.displayName
                        })
                formatter?.locale?.hashCode()?.let {
                    container.check(it)
                }
            }
            container.setOnCheckedChangeListener { group, checkedId ->
                (group.findViewById<Chip>(checkedId).tag as? Locale)?.let {
                    changeLocale(it)
                }
            }
        }

        findViewById<ExtendedFloatingActionButton>(R.id.fab)?.apply {
            setOnClickListener {
                cashOut()
            }
        }
    }

    private fun cashOut() {
        if (formatter!!.numberValue <= BigDecimal.ZERO)
            return
        Snackbar.make(
            window.findViewById(android.R.id.content),
            "Cashed out ${formatter?.textValue}!",
            Snackbar.LENGTH_LONG
        ).setAnchorView(findViewById<ExtendedFloatingActionButton>(R.id.fab))
            .setBackgroundTint(Color.BLACK)
            .setTextColor(Color.WHITE)
            .show()
        formatter?.setAmount("0")
    }

    /**
     * We can do this a few different ways:
     *
     * 1. formatter.setLocale(locale)
     *      will update just the locale and keep the previous currency and symbol
     *
     * 2. formatter.setCurrencyFormatter(CurrencyFormatter.getInstance(locale))
     *      will set a new formatter with currency and symbol derived from the locale
     *
     * 3. formatter.setCurrencyFormatter(CurrencyFormatter.getInstance(currency, symbol, locale))
     *      will set a new formatter with all new values
     *
     * @see CurrencyFormatter.getInstance(locale)
     */
    private fun changeLocale(locale: Locale) {
        formatter?.setAmount("0")
        formatter?.setCurrencyFormatter(CurrencyFormatter.getInstance(locale))
    }

    override fun onDestroy() {
        formatter = null
        super.onDestroy()
    }

    companion object {
        val locales = listOf(
            Locale.US,
            Locale.FRANCE,
            Locale.TAIWAN,
            Locale.CHINA,
            Locale("pt", "BR")
        )
    }
}