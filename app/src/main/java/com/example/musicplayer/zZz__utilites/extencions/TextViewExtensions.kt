package com.example.musicplayer.zZz__utilites.extencions

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * Extension method to set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            start + substring.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannable
    } catch (e: Exception) {
        e.printStackTrace()
        Timber.d("exception in setColorOfSubstring, text=$text, substring=$substring")
    }
}
