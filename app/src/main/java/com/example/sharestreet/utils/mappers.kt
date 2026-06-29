package com.example.sharestreet.utils

import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.example.sharestreet.domainLayer.model.UserModel
import com.google.firebase.auth.FirebaseUser

fun UserDto.toUserModel()  = UserModel(
    uid = this.uid,
    email = this.email?:"Unknown",
    displayName = this.displayName?:"Unknown",
    isVerified = this.isVerified
)
fun FirebaseUser.toDto():UserDto {
    return UserDto(
        uid = this.uid,
        email = this.email,
        displayName = this.displayName,
        isVerified = this.isEmailVerified
    )
}