package com.example.currency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.currencylibrary.CurrencyFormatter
import com.example.currencylibrary.CurrencyTextWatcher
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currencyTextWatcher: CurrencyTextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.gift)?.let { editText ->
            currencyTextWatcher = CurrencyTextWatcher(editText).also {
                it.setFormatter(
                    CurrencyFormatter.getInstance(
                        Currency.getInstance(Locale.US),
                        "💸", // show setting a custom symbol
                        Locale.US
                    )
                )
            }
            editText.addTextChangedListener(currencyTextWatcher)
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
                "Cashed out ${currencyTextWatcher?.amount}!",
                Snackbar.LENGTH_LONG
            ).setAnchorView(it).show()
            currencyTextWatcher?.setAmount("")
        }
    }

    private fun onChangeCurrency(locale: Locale) {
        currencyTextWatcher?.setFormatter(CurrencyFormatter.getInstance(locale))
    }

    override fun onDestroy() {
        currencyTextWatcher = null
        super.onDestroy()
    }

    companion object {
        val locales = listOf(Locale.US, Locale.FRANCE, Locale.TAIWAN, Locale.CHINA)
    }
}