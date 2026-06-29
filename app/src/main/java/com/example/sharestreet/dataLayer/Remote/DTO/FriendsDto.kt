package com.example.sharestreet.dataLayer.Remote.DTO

data class FriendsDto(
    val userId : String?,
    val friendsList: List<String> = emptyList(),
    val allowedFriend : List<String> = emptyList()
)