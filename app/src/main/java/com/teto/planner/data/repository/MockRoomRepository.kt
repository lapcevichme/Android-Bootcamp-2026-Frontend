package com.teto.planner.data.repository

import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.repository.RoomRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockRoomRepository @Inject constructor() : RoomRepository {
    private val rooms = listOf(
        Room("room-1", "Переговорная", 8),
        Room("room-2", "Большая переговорная", 12),
        Room("room-3", "Кладовка", 4),
        Room("room-4", "Зал", 20)
    )

    override suspend fun listRooms(page: Int, size: Int): Result<PagedList<Room>> {
        delay(500)
        return Result.success(PagedList(rooms, PageMeta(page, size, rooms.size)))
    }

    override suspend fun listAvailableRooms(
        date: LocalDate,
        startHour: Int,
        capacity: Int?
    ): Result<PagedList<Room>> {
        delay(600)
        val filteredRooms = if (capacity != null) {
            rooms.filter { it.capacity >= capacity }
        } else {
            rooms
        }
        return Result.success(PagedList(filteredRooms, PageMeta(0, 50, filteredRooms.size)))
    }
}