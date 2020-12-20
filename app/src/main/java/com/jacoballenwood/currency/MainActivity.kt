package com.jacoballenwood.currency

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jacoballenwood.formatter.MultiCurrencyFormatter
import com.jacoballenwood.formatter.util.CurrencyFormatter
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    private var formatter: MultiCurrencyFormatter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.gift)?.let { editText ->
            formatter = MultiCurrencyFormatter.newInstance(this, editText)
                .setLocale(Locale.US)
                .setSymbol("💸") // set custom symbol
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