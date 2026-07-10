package com.example.sharestreet.di.Instances

import com.example.sharestreet.dataLayer.Repository.LocationRepositoryImpl
import com.example.sharestreet.domainLayer.inteface.LocationRepository
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    companion object{
        @Provides
        @Singleton
        fun provideRTDB(): FirebaseDatabase{
            return Firebase.database
        }
    }

    @Binds
    abstract fun getLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}