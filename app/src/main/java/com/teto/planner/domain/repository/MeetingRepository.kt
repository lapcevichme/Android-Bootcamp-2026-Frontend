package com.teto.planner.domain.repository

import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.meeting.MeetingParticipant
import com.teto.planner.domain.model.user.ParticipantStatus
import java.time.LocalDate

interface MeetingRepository {
    suspend fun getMeetings(
        startDate: LocalDate,
        endDate: LocalDate,
        includePending: Boolean = false
    ): Result<PagedList<Meeting>>

    suspend fun getMeeting(id: String): Result<Meeting>

    suspend fun createMeeting(
        date: LocalDate,
        startTimeHour: Int,
        durationHours: Int,
        title: String,
        description: String?,
        roomId: String?,
        participantIds: List<String>
    ): Result<Meeting>

    suspend fun cancelMeeting(id: String): Result<Unit>

    suspend fun listInvitations(
        status: ParticipantStatus = ParticipantStatus.PENDING
    ): Result<PagedList<Invitation>>

    suspend fun respondToInvitation(
        meetingId: String,
        status: ParticipantStatus
    ): Result<MeetingParticipant>
}