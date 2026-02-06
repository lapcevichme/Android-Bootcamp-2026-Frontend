package com.teto.planner.di

import android.util.Base64
import android.util.Log
import com.teto.planner.data.local.CredentialsHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://teto-planner.fly.dev/"

    @Provides
    @Singleton
    fun provideHttpClient(credentialsHolder: CredentialsHolder): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            defaultRequest {
                url(BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor-Network", message)
                    }
                }
                level = LogLevel.ALL
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                if (request.url.encodedPath != "/api/auth/login") {
                    val creds = credentialsHolder.credentialsFlow.value

                    if (creds != null) {
                        val authString = "${creds.login}:${creds.pass}"
                        val encodedAuth = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)
                        request.header(HttpHeaders.Authorization, "Basic $encodedAuth")
                    }
                }

                execute(request)
            }
        }
    }
}