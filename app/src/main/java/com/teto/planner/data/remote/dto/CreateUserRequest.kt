package com.teto.planner.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val login: String,
    val name: String,
    val password: String,
    val telegramNick: String? = null
)