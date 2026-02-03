package com.teto.planner.data.repository

import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.user.LoadStatus
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockUserRepository @Inject constructor() : UserRepository {
    private var currentUserMe = UserMe(
        id = "user-1",
        login = "ivan_p",
        name = "Иван Петров",
        bio = "Backend разработчик, люблю архитектуру",
        telegram = "@ivan_p",
        avatarUrl = null,
        roles = emptyList()
    )

    private val allUsers = listOf(
        UserSummary("user-1", "Иван Петров", null, 4, LoadStatus.MEDIUM),
        UserSummary("user-2", "Анна Сидорова", null, 2, LoadStatus.LOW),
        UserSummary("user-3", "Сергей Волков", null, 6, LoadStatus.HIGH),
        UserSummary("user-4", "Мария Кот", null, 0, LoadStatus.LOW),
        UserSummary("user-5", "Алексей Кузнецов", null, 1, LoadStatus.LOW),
        UserSummary("user-6", "Елена Соколова", null, 8, LoadStatus.HIGH)
    )

    override suspend fun getMe(): Result<UserMe> {
        delay(500)
        return Result.success(currentUserMe)
    }

    override suspend fun updateMe(name: String?, bio: String?, telegram: String?): Result<UserMe> {
        delay(800)
        currentUserMe = currentUserMe.copy(
            name = name ?: currentUserMe.name,
            bio = bio ?: currentUserMe.bio,
            telegram = telegram ?: currentUserMe.telegram
        )
        return Result.success(currentUserMe)
    }

    override suspend fun listUsers(
        query: String?,
        page: Int,
        size: Int
    ): Result<PagedList<UserSummary>> {
        delay(600)

        val filtered = if (query.isNullOrBlank()) {
            allUsers
        } else {
            allUsers.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        val start = page * size
        val items = filtered.drop(start).take(size)

        return Result.success(
            PagedList(
                items = items,
                meta = PageMeta(page, size, filtered.size)
            )
        )
    }
}