package com.example.demoapp.di

import com.example.demoapp.data.repository.AchievementRepositoryImpl
import com.example.demoapp.data.repository.AiRepositoryImpl
import com.example.demoapp.data.repository.CloudinaryRepositoryImpl
import com.example.demoapp.data.repository.CommentRepositoryImpl
import com.example.demoapp.data.repository.NotificationRepositoryImpl
import com.example.demoapp.data.repository.PetRepositoryImpl
import com.example.demoapp.data.repository.UserRepositoryImpl
import com.example.demoapp.domain.repository.AchievementRepository
import com.example.demoapp.domain.repository.AiRepository
import com.example.demoapp.domain.repository.CloudinaryRepository
import com.example.demoapp.domain.repository.CommentRepository
import com.example.demoapp.domain.repository.NotificationRepository
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds @Singleton
    abstract fun bindPetRepository(impl: PetRepositoryImpl): PetRepository

    @Binds @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository

    @Binds @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds @Singleton
    abstract fun bindAchievementRepository(impl: AchievementRepositoryImpl): AchievementRepository

    @Binds @Singleton
    abstract fun bindCloudinaryRepository(impl: CloudinaryRepositoryImpl): CloudinaryRepository

    @Binds @Singleton
    abstract fun bindAiRepository(impl: AiRepositoryImpl): AiRepository
}
