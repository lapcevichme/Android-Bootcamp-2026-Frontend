package com.teto.planner.domain.repository

import com.teto.planner.domain.model.meeting.BusySlot
import com.teto.planner.domain.model.meeting.IntersectionGrid
import java.time.LocalDate

interface SchedulingRepository {
    suspend fun getMyBusySlots(date: LocalDate): Result<List<BusySlot>>

    suspend fun getIntersectionGrid(
        date: LocalDate,
        userIds: List<String>
    ): Result<IntersectionGrid>
}