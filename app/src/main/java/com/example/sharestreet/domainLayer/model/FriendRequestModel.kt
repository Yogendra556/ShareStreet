package com.example.sharestreet.domainLayer.model

data class FriendRequestModel(
    val reqestUid:String,
    val senderId:String,
    val receiverId:String,
    val status: String
)