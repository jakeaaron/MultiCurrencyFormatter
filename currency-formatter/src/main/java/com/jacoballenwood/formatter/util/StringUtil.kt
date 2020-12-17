package com.jacoballenwood.formatter.util

object StringUtil {
    fun indexOfLastDigit(str: String?): Int {
        var result = 0
        str ?: return result
        for (i in str.indices) {
            if (Character.isDigit(str[i])) {
                result = i
            }
        }
        return result
    }
}