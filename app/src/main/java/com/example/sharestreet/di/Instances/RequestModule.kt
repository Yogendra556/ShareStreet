package com.example.sharestreet.di.Instances

import com.example.sharestreet.dataLayer.Repository.FriendRequestRepoImpl
import com.example.sharestreet.domainLayer.inteface.FriendRequestInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RequestModule {
    @Binds
    abstract fun bindFriendRequestRepo(
        impl: FriendRequestRepoImpl
    ): FriendRequestInterface

}