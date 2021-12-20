package com.example.skgcode_teamb.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    private val apiDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val appDateFormat = "MMMM dd, yyyy"
    private val appTimeFormat = "h:mm a"

    // Convert Api Date format to App Date format
    @SuppressLint("SimpleDateFormat")
    fun apiDateToAppDate(api_date: String): String {
        val parser = SimpleDateFormat(apiDateFormat)
        val formatter = SimpleDateFormat(appDateFormat, Locale.UK)

        return formatter.format(parser.parse(api_date)!!)
    }

    // Convert Api Date format to MilliSeconds
    @SuppressLint("SimpleDateFormat")
    fun apiDateToMilliSeconds(api_date: String): Long {
        val parser = SimpleDateFormat(apiDateFormat)
        parser.timeZone = TimeZone.getTimeZone("UTC")

        return parser.parse(api_date)!!.time
    }

    // Convert Api Time format to App Time format
    @SuppressLint("SimpleDateFormat")
    fun apiTimeToAppTime(api_time: String): String {
        val parser = SimpleDateFormat(apiDateFormat)
        val formatter = SimpleDateFormat(appTimeFormat, Locale.UK)

        return formatter.format(parser.parse(api_time)!!)
    }

    // Convert App Date format to Api Date format
    @SuppressLint("SimpleDateFormat")
    fun appDateToApiDate(app_date: String): String {
        val parser = SimpleDateFormat(appDateFormat, Locale.UK)
        val formatter = SimpleDateFormat(apiDateFormat)

        return formatter.format(parser.parse(app_date)!!)
    }

    // Convert MilliSeconds to Api Date format
    @SuppressLint("SimpleDateFormat")
    fun milliSecondsToApiDate(milli_seconds: Long): String {
        val parser = SimpleDateFormat(apiDateFormat)

        return parser.format(Date(milli_seconds))
    }

    // Convert MilliSeconds to App Date format
    fun milliSecondsToAppDate(milli_seconds: Long): String {
        val parser = SimpleDateFormat(appDateFormat, Locale.UK)

        return parser.format(Date(milli_seconds))
    }

}