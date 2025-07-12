package com.example.shitzbank.common.utils.datetime

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun LocalDateTime.toIsoZString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return this.atOffset(ZoneOffset.UTC).format(formatter)
}