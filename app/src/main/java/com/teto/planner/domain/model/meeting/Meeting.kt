package com.teto.planner.domain.model.meeting

import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.user.UserSummary
import java.time.LocalDate
import java.time.LocalTime

data class Meeting(
    val id: String,
    val organizer: UserSummary,
    val title: String,
    val description: String?,
    val date: LocalDate,
    val startTime: LocalTime,
    val durationHours: Int,
    val room: Room?,
    val status: MeetingStatus,
    val participants: List<MeetingParticipant>
) {
    val endTime: LocalTime get() = startTime.plusHours(durationHours.toLong())
}