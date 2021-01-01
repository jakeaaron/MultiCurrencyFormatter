package com.jacoballenwood.formatter.main

import android.text.TextWatcher

interface TextChangeListener : Listeners<TextWatcher>

interface Listeners<T> {
    val listeners: List<T>
    fun addListener(listener: T)
    fun removeListener(listener: T)
    fun clear()
}

class ListenersImpl<T> : Listeners<T> {

    private val _listeners = mutableListOf<T>()
    override val listeners: List<T>
        get() = _listeners

    override fun addListener(listener: T) {
        _listeners.add(listener)
    }

    override fun removeListener(listener: T) {
        _listeners.remove(listener)
    }

    override fun clear() {
        _listeners.clear()
    }

}

internal fun <T> Listeners<T>.notify(block: (T) -> Unit) {
    listeners.forEach(block)
}