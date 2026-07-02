package com.example.sharestreet.dataLayer.Remote.DTO

data class FriendRequestDto(
    val senderId : String,
    val receiverId : String,
    val status : String
)