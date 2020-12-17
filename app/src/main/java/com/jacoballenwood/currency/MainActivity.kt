package com.jacoballenwood.currency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.snackbar.Snackbar
import com.jacoballenwood.formatter.CurrencyFormatter
import com.jacoballenwood.formatter.MultiCurrencyFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private var formatter: MultiCurrencyFormatter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.gift)?.let { editText ->
            formatter = MultiCurrencyFormatter.newInstance(this, editText)
                .setCurrencyFormatter(
                    CurrencyFormatter.getInstance(
                        Currency.getInstance(Locale.US),
                        "💸", // show setting a custom symbol
                        Locale.US
                    )
                )
        }

        findViewById<FlexboxLayout>(R.id.btnContainer)?.let { container ->
            locales.forEach { locale ->
                container.addView(Button(container.context).apply {
                    setOnClickListener {
                        onChangeCurrency(locale)
                    }
                    text = locale.displayName
                })
            }
        }

        findViewById<View>(R.id.fab)?.setOnClickListener {
            Snackbar.make(
                window.findViewById(android.R.id.content),
                "Cashed out ${formatter?.textValue}!",
                Snackbar.LENGTH_LONG
            ).setAnchorView(it).show()
            formatter?.setAmount("0")
        }
    }

    private fun onChangeCurrency(locale: Locale) {
        formatter?.setCurrencyFormatter(CurrencyFormatter.getInstance(locale))
    }

    override fun onDestroy() {
        formatter = null
        super.onDestroy()
    }

    companion object {
        val locales = listOf(Locale.US, Locale.FRANCE, Locale.TAIWAN, Locale.CHINA)
    }
}