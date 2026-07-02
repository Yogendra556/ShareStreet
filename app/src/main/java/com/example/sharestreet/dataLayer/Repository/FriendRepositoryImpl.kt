package com.example.sharestreet.dataLayer.Repository

import com.example.sharestreet.dataLayer.Remote.Firebase.FriendRemoteSource
import com.example.sharestreet.domainLayer.inteface.FriendsRepostoryInterface
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendRemoteSource: FriendRemoteSource
): FriendsRepostoryInterface {

    override suspend fun addFriend(userId:String,friendId:String){
        friendRemoteSource.addFriend(userId,friendId)
    }

    override suspend fun removeFriend(userId: String,friendId: String){
        friendRemoteSource.removeFriend(userId,friendId)
    }

    override suspend fun getFriendList(userId: String):List<String>?{
        return friendRemoteSource.getFriendsList(userId)
    }
}