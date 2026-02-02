package com.teto.planner.data.repository

import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.meeting.MeetingParticipant
import com.teto.planner.domain.model.meeting.MeetingStatus
import com.teto.planner.domain.model.user.LoadStatus
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.domain.repository.MeetingRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockMeetingRepository @Inject constructor() : MeetingRepository {

    private val mockUser = UserSummary(
        id = "user-1",
        name = "Иван Петров",
        avatarUrl = null,
        busyHours = 4,
        loadStatus = LoadStatus.MEDIUM
    )

    private val mockRoom = Room(id = "room-1", name = "Переговорка 'Алтай'", capacity = 8)

    private val meetings = mutableListOf(
        Meeting(
            id = "m-1",
            organizer = mockUser,
            title = "Daily Sync",
            description = "Обсуждение планов на день",
            date = LocalDate.now(),
            startTime = LocalTime.of(10, 0),
            durationHours = 1,
            room = mockRoom,
            status = MeetingStatus.SCHEDULED,
            participants = emptyList()
        ),
        Meeting(
            id = "m-2",
            organizer = mockUser,
            title = "Design Review",
            description = "Смотрим новые макеты в Figma",
            date = LocalDate.now(),
            startTime = LocalTime.of(14, 0),
            durationHours = 2,
            room = mockRoom,
            status = MeetingStatus.SCHEDULED,
            participants = emptyList()
        ),
        Meeting(
            id = "m-3",
            organizer = mockUser,
            title = "Кофе-брейк",
            description = null,
            date = LocalDate.now().plusDays(1),
            startTime = LocalTime.of(11, 0),
            durationHours = 1,
            room = null,
            status = MeetingStatus.SCHEDULED,
            participants = emptyList()
        )
    )

    override suspend fun getMeetings(
        startDate: LocalDate,
        endDate: LocalDate,
        includePending: Boolean
    ): Result<PagedList<Meeting>> {
        delay(1000)

        val filtered = meetings.filter {
            (it.date.isEqual(startDate) || it.date.isAfter(startDate)) &&
                    (it.date.isEqual(endDate) || it.date.isBefore(endDate))
        }

        return Result.success(
            PagedList(
                items = filtered,
                meta = PageMeta(0, 50, filtered.size)
            )
        )
    }

    override suspend fun getMeeting(id: String): Result<Meeting> {
        delay(500)
        val meeting = meetings.find { it.id == id }
        return if (meeting != null) Result.success(meeting) else Result.failure(Exception("Not found"))
    }

    override suspend fun createMeeting(
        date: LocalDate,
        startTimeHour: Int,
        durationHours: Int,
        title: String,
        description: String?,
        roomId: String?,
        participantIds: List<String>
    ): Result<Meeting> {
        delay(1000)
        val newMeeting = Meeting(
            id = java.util.UUID.randomUUID().toString(),
            organizer = mockUser,
            title = title,
            description = description,
            date = date,
            startTime = LocalTime.of(startTimeHour, 0),
            durationHours = durationHours,
            room = if (roomId != null) mockRoom else null,
            status = MeetingStatus.SCHEDULED,
            participants = emptyList()
        )
        meetings.add(newMeeting)
        return Result.success(newMeeting)
    }

    override suspend fun cancelMeeting(id: String): Result<Unit> {
        delay(500)
        meetings.removeIf { it.id == id }
        return Result.success(Unit)
    }

    override suspend fun listInvitations(status: ParticipantStatus): Result<PagedList<Invitation>> {
        delay(800)
        return Result.success(PagedList(emptyList(), PageMeta(0, 50, 0)))
    }

    override suspend fun respondToInvitation(
        meetingId: String,
        status: ParticipantStatus
    ): Result<MeetingParticipant> {
        delay(500)
        return Result.failure(Exception("Not implemented"))
    }
}