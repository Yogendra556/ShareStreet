package com.example.sharestreet.presentation.Location

import com.example.sharestreet.domainLayer.model.UserModel

data class LocationAccessState(
    val user: UserModel?,
    val allowed: Boolean =false
)