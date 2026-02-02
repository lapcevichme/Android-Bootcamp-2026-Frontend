package com.teto.planner.data.repository

import com.teto.planner.data.remote.dto.MeetingsPageDto
import com.teto.planner.data.remote.dto.toDomain
import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.meeting.MeetingParticipant
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.repository.MeetingRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.LocalDate
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : MeetingRepository {

    override suspend fun getMeetings(
        startDate: LocalDate,
        endDate: LocalDate,
        includePending: Boolean
    ): Result<PagedList<Meeting>> {
        return runCatching {
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
    }

    override suspend fun getMeeting(id: String) = TODO()
    override suspend fun createMeeting(
        date: LocalDate,
        startTimeHour: Int,
        durationHours: Int,
        title: String,
        description: String?,
        roomId: String?,
        participantIds: List<String>
    ): Result<Meeting> = TODO()
    override suspend fun cancelMeeting(id: String) = TODO()
    override suspend fun listInvitations(status: ParticipantStatus): Result<PagedList<Invitation>> = TODO()
    override suspend fun respondToInvitation(
        meetingId: String,
        status: ParticipantStatus
    ): Result<MeetingParticipant> = TODO()
}