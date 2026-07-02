package com.example.sharestreet.di.Instances

import com.example.sharestreet.dataLayer.Repository.FriendRepositoryImpl
import com.example.sharestreet.domainLayer.inteface.FriendsRepostoryInterface
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FriendsModule {

    @Binds
    abstract fun bindsFriendsRepositoryImpl(
        impl : FriendRepositoryImpl
    ): FriendsRepostoryInterface

    companion object{
        @Provides
        @Singleton
        fun provideFireStore(): FirebaseFirestore{
            return Firebase.firestore
        }
    }
}