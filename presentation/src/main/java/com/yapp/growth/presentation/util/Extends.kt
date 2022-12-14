package com.yapp.growth.presentation.util

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

internal val PARSE_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
internal val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

fun CalendarDay.toFormatDate(): String {
    return DATE_FORMAT.format(this.date)
}

fun CalendarDay.toParseFormatDate(): String {
    return PARSE_DATE_FORMAT.format(this.date)
}

fun Date.toFormatDate(): String {
    return DATE_FORMAT.format(this)
}

fun Date.toHour(): String {
    return SimpleDateFormat("aa h시", Locale.KOREA).format(this)
}

fun Date.toHourAndMinute(): String {
    return SimpleDateFormat("aa h시 m분", Locale.KOREA).format(this)
}

fun Date.toCalculateDiffDay(other: Date): Long {
    return (other.time - this.time) / (60 * 60 * 24 * 1000)
}

fun String.toDate(): Date {
    return PARSE_DATE_FORMAT.parse(this) ?: Date()
}

fun String.toDay(): String {
    val dateTimeFormat = SimpleDateFormat("M/d", Locale.KOREA)
    val displayText = PARSE_DATE_FORMAT.parse(this) ?: ""
    return dateTimeFormat.format(displayText)
}

fun String.toHour(): String {
    val dateTimeFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
    val displayText = PARSE_DATE_FORMAT.parse(this) ?: ""
    return dateTimeFormat.format(displayText)
}

fun String.getCurrentBlockDate(count: Int): String {
    val currentTime = this
    val calendar = Calendar.getInstance().apply {
        time = PARSE_DATE_FORMAT.parse(currentTime) ?: Date()
    }
    repeat(count) {
        calendar.add(Calendar.MINUTE, 30)
    }
    return PARSE_DATE_FORMAT.format(calendar.time)
}

fun String.toDayOfWeek(): String {
    val currentTime = this
    val calendar = Calendar.getInstance().apply {
        time = PARSE_DATE_FORMAT.parse(currentTime) ?: Date()
    }
    return DayOfWeek.values()[calendar.get(Calendar.DAY_OF_WEEK)].dayOfWeek
}

fun String.to12HourClock(): String {
    val currentTime = this
    val calendar = Calendar.getInstance().apply {
        time = PARSE_DATE_FORMAT.parse(currentTime) ?: Date()
    }
    return AM_PM.values()[calendar.get(Calendar.AM_PM)].am_pm
}

fun String.toPlanDate(): String {
    val dateTimeFormat: SimpleDateFormat
    val currentTime = this
    val calendar = Calendar.getInstance().apply {
        time = PARSE_DATE_FORMAT.parse(currentTime) ?: Date()
    }

    dateTimeFormat = if (calendar.get(Calendar.MINUTE) == 0) {
        SimpleDateFormat("M월 d일(${this.toDayOfWeek()}) aa h시", Locale.KOREA)
    } else {
        SimpleDateFormat("M월 d일(${this.toDayOfWeek()}) aa h시 mm분", Locale.KOREA)
    }

    val displayText = PARSE_DATE_FORMAT.parse(currentTime) ?: ""
    return dateTimeFormat.format(displayText)
}


enum class DayOfWeek(val dayOfWeek: String) {
    UNKNOWN("몰라"), SUNDAY("일"),
    MONDAY("월"), TUESDAY("화"),
    WEDNESDAY("수"), THURSDAY("목"),
    FRIDAY("금"), SATURDAY("토")
}

enum class AM_PM(val am_pm: String) {
    MORNING("오전"), AFTERNOON("오후")
}
