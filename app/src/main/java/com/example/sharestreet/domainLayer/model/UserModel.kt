package com.example.sharestreet.domainLayer.model

import com.example.sharestreet.dataLayer.Remote.DTO.UserDto

data class UserModel(
    val uid : String,
    val email : String,
    val displayName : String,
    val isVerified : Boolean,
)

