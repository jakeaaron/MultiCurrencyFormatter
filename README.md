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
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Then add this dependency to your app build.gradle file.

```
dependencies {
  implementation 'com.github.adawoud:BottomSheetTimeRangePicker:latest-release'
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

### Basic Usage

The `MultiCurrencyFormatter` requires a `LifecycleOwner` and an `EditText` to create a new instance. This is all that is required to start formatting currency text.

```kotlin
MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
```

`MultiCurrencyFormatter` listens for the `LifecycleOwner#onDestroy` event to clear out its reference to the `EditText`. This means that the consumer doesn't need to worry about manually clearing out the view reference. When `MultiCurrencyFormatter` is instantiated, it registers the internal `CurrencyTextWatcher` with the `EditText` in order to listen to text input changes.

By default, the `MultiCurrencyFormatter` uses the default locale associated with the device to parse and format values. The locale dictates the `Currency` value, which then dictates the currency symbol. This means that in most cases, changing the locale will end up doing the correct thing in relation to the `Currency` and symbol. However, there is additional functionality in case more granular usage is needed.

### Setting Formatter Attributes

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

### Reading and Writing Currency Values

`MultiCurrencyFormatter.textValue` holds the formatted `String` information associated with the `EditText`, including the currency symbol. 

`MultiCurrencyFormatter.numberValue` holds the `BigDecimal` value associated with the parsed `EditText.text` value.

`MultiCurrencyFormatter.setAmount` sets the text value.

For example:

```kotlin

MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
    .setLocale(Locale.US)
    .setAmount("50.00)
    
MultiCurrencyFormatter.textValue // $50.00
MultiCurrencyFormatter.numberValue // BigDecimal("50.00")

```

## Advanced Usage

In case you need to extend the `TextWatcher` in order to listen to text change events, you can extend `CurrencyTextWatcher` and pass it to `MultiCurrencyFormatter` when creating a new instance.

```kotlin



```

## How to test the software

Run the [tests](/currency-formatter/src/androidTest/java/com/jacoballenwood/formatter/) using an IDE like Intellij or Android Studio.

See [how to run tests](https://developer.android.com/studio/test) from Android Developers documentation.

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.


----

## Open source licensing info
[LICENSE](LICENSE)


----

## Credits and references

1. Projects that inspired you
2. Related projects
3. Books, papers, talks, or other sources that have meaningful impact or influence on this project
