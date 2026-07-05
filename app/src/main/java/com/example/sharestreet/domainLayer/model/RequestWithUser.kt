package com.example.sharestreet.domainLayer.model

data class RequestWithUser(
    val user : UserModel,
    val requestId : String
)