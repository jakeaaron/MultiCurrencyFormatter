# Multi-Currency Formatter

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

The MultiCurrencyFormatter allows configuration for the following fields/behaviors:

1. Currency: Java Currency class used for formatting currency text values
2. Currency symbol: Custom symbol to override the symbol associated with the Currency
3. Locale: Locale used for formatting currency text values
4. Resize text automatically
5. Superscript the currency symbol

The following code snippet shows how to configure each field/behavior:

```
MultiCurrencyFormatter.newInstance(viewLifecycleOwner, editText)
    .setCurrency(Currency.getInstance(Locale.JAPAN))
    .setLocale(Locale.US)
    .setSymbol("💸") 
    .setAutoResize(true)
    .setSymbolSuperscript(true)
```

## Usage

Show users how to use the software.
Be specific.
Use appropriate formatting when showing code snippets.

## How to test the software

If the software includes automated tests, detail how to run those tests.

## Known issues

Document any known significant shortcomings with the software.

## Getting help

Instruct users how to get help with this software; this might include links to an issue tracker, wiki, mailing list, etc.

**Example**

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

This section should detail why people should get involved and describe key areas you are
currently focusing on; e.g., trying to get feedback on features, fixing certain bugs, building
important pieces, etc.

General instructions on _how_ to contribute should be stated with a link to [CONTRIBUTING](CONTRIBUTING.md).


----

## Open source licensing info
1. [TERMS](TERMS.md)
2. [LICENSE](LICENSE)
3. [CFPB Source Code Policy](https://github.com/cfpb/source-code-policy/)


----

## Credits and references

1. Projects that inspired you
2. Related projects
3. Books, papers, talks, or other sources that have meaningful impact or influence on this project
