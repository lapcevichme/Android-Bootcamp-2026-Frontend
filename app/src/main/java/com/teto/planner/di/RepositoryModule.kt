package com.teto.planner.di

import com.teto.planner.data.repository.AuthRepositoryImpl
import com.teto.planner.data.repository.MockMeetingRepository
import com.teto.planner.data.repository.MockRoomRepository
import com.teto.planner.data.repository.MockUserRepository
import com.teto.planner.domain.repository.AuthRepository
import com.teto.planner.domain.repository.MeetingRepository
import com.teto.planner.domain.repository.RoomRepository
import com.teto.planner.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        impl: MockMeetingRepository
    ): MeetingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: MockUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        impl: MockRoomRepository
    ): RoomRepository
}