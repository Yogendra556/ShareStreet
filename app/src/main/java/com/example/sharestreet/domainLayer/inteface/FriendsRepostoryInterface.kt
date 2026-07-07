package com.example.sharestreet.domainLayer.inteface

import kotlinx.coroutines.flow.Flow


interface FriendsRepostoryInterface {

    suspend fun addFriend(userId:String,friendId: String)
    suspend fun removeFriend(userId: String, friendId: String)
    fun getFriendList(userId: String): Flow<List<String>>
}