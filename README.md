[![](https://jitpack.io/v/jakeaaron/MultiCurrencyFormatter.svg)](https://jitpack.io/#jakeaaron/MultiCurrencyFormatter) [![codecov](https://codecov.io/gh/jakeaaron/MultiCurrencyFormatter/branch/main/graph/badge.svg?token=77DF25LDEN)](https://codecov.io/gh/jakeaaron/MultiCurrencyFormatter)


# MultiCurrencyFormatter

A small Android library that dynamically reformats user input (from an `EditText`) for displaying currency values in any locale or currency. Java's `DecimalFormat` is used for formatting the numbers derived from user input.

<img src="/currency-formatter.gif" height="700" />


## Requirements

- Android 4.1 (API 16)

## Installation

Add Jitpack to your project build.gralde file
 
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Then add this dependency to your app build.gradle file.

```
dependencies {
    implementation 'com.github.jakeaaron:MultiCurrencyFormatter:1.0.0-alpha.1'
}
```

## Configuration

The `MultiCurrencyFormatter` allows configuration for the following fields/behaviors:

1. Currency: Java Currency class used for formatting currency text values
2. Currency symbol: Custom symbol to override the symbol associated with the Currency
3. Locale: Locale used for formatting currency text values
4. Resize text automatically
5. Superscript the currency symbol

The following code snippet shows how to configure each field/behavior:

```kotlin
MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
    .setCurrency(Currency.getInstance(Locale.JAPAN))
    .setLocale(Locale.US)
    .setSymbol("💸") 
    .setAutoResize(true)
    .setSymbolSuperscript(true)
```

## Usage

> Run the [sample project](/app/src/main/java/com/jacoballenwood/currency/MainActivity.kt) to see the `MultiCurrencyFormatter` in action.

#### Basic Usage

The `MultiCurrencyFormatter` requires a `LifecycleOwner` and an `EditText` to create a new instance. This is all that is required to start formatting currency text.

```kotlin
MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
```

`MultiCurrencyFormatter` listens for the `LifecycleOwner#onDestroy` event to clear out its reference to the `EditText` to avoid leaking its `context`. This means that the consumer doesn't need to worry about manually clearing out the view reference. When `MultiCurrencyFormatter` is instantiated, it registers the internal `CurrencyTextWatcher` with the `EditText` in order to listen to text input changes.

By default, the `MultiCurrencyFormatter` uses the default locale associated with the device to parse and format values. The locale dictates the `Currency` value, which then dictates the currency symbol. This means that in most cases, changing the locale will end up doing the correct thing in relation to the `Currency` and symbol. However, there is additional functionality in case more granular control is needed.

#### Setting Formatter Attributes

In order to change the `Currency`, `Symbol`, or `Locale` used by the formatter, use the corresponding methods on `MultiCurrencyFormatter` instance:

```kotlin
MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
    .setLocale(Locale.CHINA)
    .setSymbol("¥")
    .setCurrency(Currency.getInstance(Locale.CHINA))
```

or

```kotlin
MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
    .setCurrencyFormatter(
        CurrencyFormatter.getInstance(
            currency,
            symbol,
            locale
        )
    )
```

This is useful for updating the formatter in response to a user event (like selecting a new currency or locale from a menu/list, i.e. see [sample project](/app/src/main/java/com/jacoballenwood/currency/MainActivity.kt)).

#### Reading and Writing Currency Values

`MultiCurrencyFormatter.textValue` holds the formatted `String` information associated with the `EditText`, including the currency symbol. 

`MultiCurrencyFormatter.numberValue` holds the `BigDecimal` value associated with the parsed `EditText.text` value.

`MultiCurrencyFormatter.setAmount` sets the text value.

For example:

```kotlin

MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
    .setLocale(Locale.US)
    .setAmount("50.00")
    
MultiCurrencyFormatter.textValue // $50.00
MultiCurrencyFormatter.numberValue // BigDecimal("50.00")

```

## Advanced Usage

#### Custom `TextWatcher`

If you need additional callbacks into the `EditText`'s `TextWatcher` use the `addTextChangeListener` method.

For example:

```kotlin

val editText = EditText(this)
val formatter = CurrencyFormatter.getInstance(Locale.ITALY)
val multiCurrencyFormatter = MultiCurrencyFormatter.newInstance(
    this,
    editText,
    currencyFormatter = formatter
)
val customListener = object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        // do something important
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
multiCurrencyFormatter.addListener(customListener)

```

You may use the `removeListener` call to remove the listener, however, all listeners will get cleared on the `LifecycleOwner#onDestroy` event. 

#### Underlying `DecimalFormat`

If you need access to the underlying `DecimalFormat` instance, it can be accessed from the `CurrencyTextWatcher` instance:

```kotlin
val multiCurrencyFormatter = MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
val decimalFormat = multiCurrencyFormatter.currencyTextWatcher.formatter.underlyingDecimalFormat
```
If you just need access to the underlying `DecimalFormat`s `decimalFormatSymbols`, use the extension field `MultiCurrencyFormatter.decimalFormatSymbols`. This is useful for accessing the `decimalSeparator`, for instance.

## How to test the software

Run the [tests](/currency-formatter/src/androidTest/java/com/jacoballenwood/formatter/) using an IDE like Intellij or Android Studio. [Learn more](https://developer.android.com/studio/test).


## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.


## Open source licensing info
[LICENSE](LICENSE)


## Credits and references

1. [Cash App](https://cash.app/)
2. [Stack Overflow Q/A](https://stackoverflow.com/questions/5107901/better-way-to-format-currency-input-edittext/8275680)
3. [CurrencyEditText](https://github.com/BlacKCaT27/CurrencyEditText)
4. [How to Format 30+ Currencies from Countries All Over the World](https://fastspring.com/blog/how-to-format-30-currencies-from-countries-all-over-the-world/)
5. [Location Based Currency Formatting in Java](https://howtodoinjava.com/java/date-time/location-based-currency-formatting-in-java/)
6. [Multi-currency support in Java](https://getaround.tech/multi-currency-java/)
