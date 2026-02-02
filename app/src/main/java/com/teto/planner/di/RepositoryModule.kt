package com.teto.planner.di

import com.teto.planner.data.repository.MeetingRepositoryImpl
import com.teto.planner.data.repository.MockMeetingRepository
import com.teto.planner.domain.repository.MeetingRepository
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
    abstract fun bindMeetingRepository(
        impl: MockMeetingRepository
    ): MeetingRepository
}