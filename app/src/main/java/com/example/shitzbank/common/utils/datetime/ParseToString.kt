package com.example.shitzbank.common.utils.datetime

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toIsoString(): String {
    return this.format(DateTimeFormatter.ISO_DATE_TIME)
}