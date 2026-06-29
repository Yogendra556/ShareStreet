package com.example.sharestreet.dataLayer.Remote.DTO

// This is the object received from firebase
data class UserDto(
    val uid : String,
    val email : String?,
    val displayName : String?,
    val isVerified : Boolean,
    val friendsList : List<UserDto>?,
    val allowedFriendList : List<UserDto>?
)

