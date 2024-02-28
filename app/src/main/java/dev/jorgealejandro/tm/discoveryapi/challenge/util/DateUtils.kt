package dev.jorgealejandro.tm.discoveryapi.challenge.util

import android.annotation.SuppressLint

object DateUtils {

    // @param "YEAR-MONTH-DAY T H:MM:SS"
    // return: $MONTH $DAY
    @SuppressLint("SimpleDateFormat")
    fun dateStringFormat(date: String?): String? {
        if (date == null) return null
        val dateValue = date.split("-", ":")
        return "${getMonth(Integer.parseInt(dateValue[1]) - 1)} ${dateValue[2].substring(0, 2)}"
    }

    private fun getMonth(monthValue: Int): String {
        val months = arrayOf(
            "JAN", "FEB", "MAR", "APR",
            "MAY", "JUN", "JUL", "AUG",
            "SEPT", "OCT", "NOV", "DEC"
        )
        return months[monthValue]
    }
}