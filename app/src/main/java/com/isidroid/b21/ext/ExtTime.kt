package com.isidroid.b21.ext

import java.text.SimpleDateFormat
import java.util.*

fun Int.toTimeMinSec() = with(Time(this)) { String.format("%02d:%02d", minutes, seconds) }
fun Int.toTimeHoursMinSec() =
    with(Time(this)) { String.format("%02d:%02d:%02d", hours, minutes, seconds) }

val Int.hasHours
    get() = (this / 3600) > 0

data class Time(
    private val totalSeconds: Int,
    val hours: Int = totalSeconds / 3600,
    val minutes: Int = (totalSeconds % 3600) / 60,
    val seconds: Int = totalSeconds % 60
) {
    val hasHours
        get() = hours > 0

    val hasMinutes
        get() = minutes > 0

}

fun Date.addDays(days: Int): Date = with(Calendar.getInstance()) {
    time = this@addDays
    add(Calendar.DATE, days)
    time
}

fun Date.addMonths(months: Int): Date = with(Calendar.getInstance()) {
    time = this@addMonths
    add(Calendar.MONTH, months)
    time
}

fun Date.addSeconds(seconds: Int): Date = with(Calendar.getInstance()) {
    time = this@addSeconds
    add(Calendar.SECOND, seconds)
    time
}

fun Date.addMinutes(minutes: Int): Date = with(Calendar.getInstance()) {
    time = this@addMinutes
    add(Calendar.MINUTE, minutes)
    time
}

val Date?.calendar: Calendar
    get() = this?.let { Calendar.getInstance().also { calendar -> calendar.time = this } } ?: Calendar.getInstance()

val String.date: Date
    get() = try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)!!
    } catch (t: Throwable) {
        Date()
    }

val Date.string: String
    get() = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(this)

fun Date.diffTime(date: Date?): Int {
    date ?: return -1

    val diff: Long = time - date.time
    return diff.toInt()
}

val Date.startDay: Date
    get() = with(Calendar.getInstance()) {
        time = this@startDay
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        time
    }
