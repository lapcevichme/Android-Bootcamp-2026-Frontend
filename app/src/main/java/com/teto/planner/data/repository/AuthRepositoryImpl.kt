package com.teto.planner.data.repository

import com.teto.planner.data.local.CredentialsHolder
import com.teto.planner.data.remote.dto.CreateUserRequest
import com.teto.planner.data.remote.dto.LoginRequest
import com.teto.planner.data.remote.dto.UserMeDto
import com.teto.planner.data.remote.dto.toDomain
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val credentialsHolder: CredentialsHolder
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<UserMe> = runCatching {
        val response = client.post("api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(login, password))
        }

        if (response.status.isSuccess()) {
            val userDto = response.body<UserMeDto>()
            credentialsHolder.setCredentials(login, password)

            userDto.toDomain()
        } else {
            val errorJson = runCatching { response.body<JsonObject>() }.getOrNull()

            val errorMessage = errorJson?.get("error")?.jsonObject?.get("message")?.jsonPrimitive?.content
                ?: "Ошибка сервера: ${response.status.value}"

            throw Exception(errorMessage)
        }
    }

    override suspend fun register(name: String, login: String, password: String, telegramNick: String?): Result<Unit> = runCatching {
        val response = client.post("api/users") {
            contentType(ContentType.Application.Json)
            setBody(
                CreateUserRequest(
                    login = login,
                    name = name,
                    password = password,
                    telegramNick = telegramNick
                )
            )
        }

        if (response.status.isSuccess()) { Unit
        } else {
            val errorJson = runCatching { response.body<JsonObject>() }.getOrNull()

            val errorMessage = errorJson?.get("error")?.jsonObject?.get("message")?.jsonPrimitive?.content
                ?: "Ошибка регистрации: ${response.status.value}"

            throw Exception(errorMessage)
        }
    }

    override fun logout() {
        credentialsHolder.clear()
    }

    override fun isLoggedIn(): Boolean {
        return credentialsHolder.hasCredentialsNow()
    }
}