package com.teto.planner.data.remote.dto

import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.meeting.MeetingParticipant
import com.teto.planner.domain.model.meeting.MeetingStatus
import com.teto.planner.domain.model.user.LoadStatus
import com.teto.planner.domain.model.user.ParticipantRole
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.model.user.UserSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class MeetingDto(
    val id: String,
    val organizer: UserSummaryDto,
    val title: String,
    val description: String?,
    @SerialName("meeting_date") val meetingDate: String,
    @SerialName("start_hour") val startHour: Int,
    @SerialName("duration_hours") val durationHours: Int,
    val status: String,
    val room: RoomDto?,
    val participants: List<MeetingParticipantDto>? = null
)

@Serializable
data class UserSummaryDto(
    val id: String,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("busy_hours") val busyHours: Int? = 0,
    @SerialName("load_status") val loadStatus: String? = "LOW"
)

@Serializable
data class RoomDto(
    val id: String,
    val name: String,
    val capacity: Int
)

@Serializable
data class MeetingParticipantDto(
    val user: UserSummaryDto,
    val role: String,
    val status: String
)

@Serializable
data class MeetingsPageDto(
    val items: List<MeetingDto>
)


fun MeetingDto.toDomain() = Meeting(
    id = id,
    organizer = organizer.toDomain(),
    title = title,
    description = description,
    date = LocalDate.parse(meetingDate),
    startTime = LocalTime.of(startHour, 0),
    durationHours = durationHours,
    room = room?.let { Room(it.id, it.name, it.capacity) },
    status = MeetingStatus.valueOf(status),
    participants = participants?.map { it.toDomain() } ?: emptyList()
)

fun UserSummaryDto.toDomain() = UserSummary(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    busyHours = busyHours ?: 0,
    loadStatus = LoadStatus.valueOf(loadStatus ?: "LOW")
)

fun MeetingParticipantDto.toDomain() = MeetingParticipant(
    user = user.toDomain(),
    role = ParticipantRole.valueOf(role),
    status = ParticipantStatus.valueOf(status)
)