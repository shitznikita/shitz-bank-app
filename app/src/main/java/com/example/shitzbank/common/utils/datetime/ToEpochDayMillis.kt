package com.example.shitzbank.common.utils.datetime

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toEpochDayMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
