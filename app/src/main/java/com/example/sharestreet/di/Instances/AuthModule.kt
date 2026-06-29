package com.example.sharestreet.di.Instances

import com.example.sharestreet.dataLayer.Repository.AuthRepositoryImpl
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// This class provides all the instances required for firebase auth
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        impl : AuthRepositoryImpl
    ): AuthRepository

    // Provides need an abstract object while bind needs abstract class hence companion object used other wise for firebasse
    // create an object then write provides inside
    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}