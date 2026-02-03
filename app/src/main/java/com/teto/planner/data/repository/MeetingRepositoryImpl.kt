package com.teto.planner.data.repository

import com.teto.planner.data.remote.dto.*
import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.meeting.*
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.repository.MeetingRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.time.LocalDate
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : MeetingRepository {

    override suspend fun getMeetings(
        startDate: LocalDate,
        endDate: LocalDate,
        includePending: Boolean
    ): Result<PagedList<Meeting>> = runCatching {
        val response = client.get("meetings") {
            parameter("startDate", startDate.toString())
            parameter("endDate", endDate.toString())
            parameter("includePending", includePending)
        }.body<MeetingsPageDto>()

        PagedList(
            items = response.items.map { it.toDomain() },
            meta = PageMeta(0, 50, response.items.size)
        )
    }

    override suspend fun getMeeting(id: String): Result<Meeting> = runCatching {
        client.get("meetings/$id").body<MeetingDto>().toDomain()
    }

    override suspend fun createMeeting(
        date: LocalDate,
        startTimeHour: Int,
        durationHours: Int,
        title: String,
        description: String?,
        roomId: String?,
        participantIds: List<String>
    ): Result<Meeting> = runCatching {
        val request = MeetingCreateRequest(
            meetingDate = date.toString(),
            startHour = startTimeHour,
            durationHours = durationHours,
            title = title,
            description = description,
            roomId = roomId,
            participantIds = participantIds
        )
        client.post("meetings") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<MeetingDto>().toDomain()
    }

    override suspend fun getIntersection(
        date: LocalDate,
        userIds: List<String>
    ): Result<IntersectionResponse> = runCatching {
        client.get("schedule/intersection") {
            parameter("meeting_date", date.toString())
            parameter("user_ids", userIds.joinToString(","))
        }.body<IntersectionResponseDto>().toDomain()
    }

    override suspend fun cancelMeeting(id: String): Result<Unit> = runCatching {
        client.delete("meetings/$id")
    }

    override suspend fun listInvitations(status: ParticipantStatus): Result<PagedList<Invitation>> = runCatching {
        val response = client.get("invitations") {
            parameter("status", status.name)
        }.body<InvitationsPageDto>()

        PagedList(
            items = response.items.map { it.toDomain() },
            meta = PageMeta(0, 50, response.items.size)
        )
    }

    override suspend fun respondToInvitation(
        meetingId: String,
        status: ParticipantStatus
    ): Result<MeetingParticipant> = runCatching {
        client.post("invitations/$meetingId/response") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("status" to status.name))
        }.body<MeetingParticipantDto>().toDomain()
    }
}