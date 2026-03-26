package com.example.demoapp.di

import com.example.demoapp.data.repository.CommentRepositoryImpl
import com.example.demoapp.data.repository.NotificationRepositoryImpl
import com.example.demoapp.data.repository.PetRepositoryImpl
import com.example.demoapp.data.repository.UserRepositoryImpl
import com.example.demoapp.domain.repository.CommentRepository
import com.example.demoapp.domain.repository.NotificationRepository
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la inyección de dependencias de los repositorios.
 * Vincula cada interfaz de repositorio con su implementación concreta.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindPetRepository(
        petRepositoryImpl: PetRepositoryImpl
    ): PetRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}
