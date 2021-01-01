package com.jacoballenwood.formatter.main

import android.text.TextWatcher

interface TextChangeListener {
    val listeners: List<TextWatcher>
    fun addTextChangeListener(watcher: TextWatcher)
    fun removeTextChangeListener(watcher: TextWatcher)
}