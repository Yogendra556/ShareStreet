package com.example.sharestreet.domainLayer.inteface


interface FriendsRepostoryInterface {

    suspend fun addFriend(userId:String,friendId: String)
    suspend fun removeFriend(userId: String, friendId: String)
    suspend fun getFriendList(userId: String): List<String>?
}