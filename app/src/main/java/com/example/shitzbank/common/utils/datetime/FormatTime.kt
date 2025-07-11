package com.example.shitzbank.common.utils.datetime

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun formatTime(time: LocalTime): String {
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}