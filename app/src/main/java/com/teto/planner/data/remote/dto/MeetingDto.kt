package com.teto.planner.data.remote.dto

import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.meeting.*
import com.teto.planner.domain.model.user.*
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
    val room: RoomDto? = null,
    val participants: List<MeetingParticipantDto>? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class UserSummaryDto(
    val id: String,
    val login: String? = null,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("busy_hours") val busyHours: Int? = 0,
    @SerialName("load_status") val loadStatus: String? = "LOW",
    @SerialName("telegram_nick") val telegramNick: String? = null
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
    val status: String,
    @SerialName("meeting_date") val meetingDate: String? = null,
    @SerialName("start_hour") val startHour: Int? = null
)

@Serializable
data class MeetingsPageDto(
    val items: List<MeetingDto>,
    val meta: PageMetaDto? = null
)

@Serializable
data class UsersPageDto(
    val items: List<UserSummaryDto>,
    val meta: PageMetaDto? = null
)

@Serializable
data class PageMetaDto(
    val page: Int,
    val size: Int,
    val total: Int
)

@Serializable
data class MeetingCreateRequest(
    @SerialName("meeting_date") val meetingDate: String,
    @SerialName("start_hour") val startHour: Int,
    @SerialName("duration_hours") val durationHours: Int = 1,
    val title: String,
    val description: String?,
    @SerialName("room_id") val roomId: String?,
    @SerialName("participant_ids") val participantIds: List<String>
)

@Serializable
data class IntersectionResponseDto(
    @SerialName("meeting_date") val meetingDate: String,
    val organizer: UserSummaryDto? = null,
    val users: List<UserSummaryDto>? = null,
    val slots: List<IntersectionSlotDto>
)

@Serializable
data class IntersectionSlotDto(
    val hour: Int,
    val status: String,
    @SerialName("conflicted_users") val conflictedUsers: List<UserSummaryDto>,
    val label: String? = null
)

@Serializable
data class InvitationDto(
    val meeting: MeetingDto,
    @SerialName("my_role") val myRole: String,
    @SerialName("my_status") val myStatus: String
)

@Serializable
data class InvitationsPageDto(
    val items: List<InvitationDto>,
    val meta: PageMetaDto? = null
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

fun IntersectionSlotDto.toDomain() = IntersectionSlot(
    hour = hour,
    status = IntersectionSlotStatus.valueOf(status),
    conflictedUsers = conflictedUsers.map { it.toDomain() },
    label = label ?: "${hour}:00"
)

fun IntersectionResponseDto.toDomain() = IntersectionResponse(
    date = LocalDate.parse(meetingDate),
    organizer = organizer?.toDomain() ?: throw IllegalStateException("Organizer required"),
    users = users?.map { it.toDomain() } ?: emptyList(),
    slots = slots.map { it.toDomain() }
)

fun InvitationDto.toDomain() = Invitation(
    meeting = meeting.toDomain(),
    myRole = ParticipantRole.valueOf(myRole),
    myStatus = ParticipantStatus.valueOf(myStatus)
)