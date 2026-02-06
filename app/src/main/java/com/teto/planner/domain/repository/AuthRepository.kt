package com.teto.planner.domain.repository

import com.teto.planner.domain.model.user.UserMe

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<UserMe>
    fun logout()
    fun isLoggedIn(): Boolean
}