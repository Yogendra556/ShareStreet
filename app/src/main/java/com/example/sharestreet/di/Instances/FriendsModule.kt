package com.example.sharestreet.di.Instances

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FriendsModule {
    companion object{
        @Provides
        @Singleton
        fun provideFireStore(): FirebaseFirestore{
            return Firebase.firestore
        }
    }
}