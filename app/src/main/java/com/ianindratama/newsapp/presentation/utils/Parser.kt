package com.ianindratama.newsapp.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun parseNewsTimestamp(timestamp: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val date = sdf.parse(timestamp) as Date

    return SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(date)
}