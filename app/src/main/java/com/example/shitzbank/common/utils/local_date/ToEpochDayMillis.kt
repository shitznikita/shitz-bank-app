package com.example.shitzbank.common.utils.local_date

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toEpochDayMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}