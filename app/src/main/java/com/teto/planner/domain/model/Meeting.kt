package com.teto.planner.domain.model

import java.time.LocalDate

// TODO марка спросить про доп поля
data class Meeting(
    val id: String,
    val title: String,
    val time: String,
    val date: LocalDate,
    val participants: List<String>
)