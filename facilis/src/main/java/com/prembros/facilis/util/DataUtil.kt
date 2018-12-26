package com.prembros.facilis.util

import android.content.Context
import androidx.annotation.IntegerRes

fun Context.intOf(@IntegerRes integerRes: Int): Int = resources.getInteger(integerRes)

fun isNullOrEmpty(s: String?): Boolean = s == null || s.isEmpty()

fun isNullOrEmpty(s: Any?): Boolean = s == null

fun isNullOrEmpty(s: CharSequence?): Boolean = s == null || s.isEmpty()

fun Int.formatInt(): String = (if (this > 9) toString() else "0${this}")

fun <T> isNullOrEmpty(c: Collection<T>?): Boolean = c == null || c.isEmpty()

fun equalsNullString(s: String?): Boolean = s == null || s == "null"
