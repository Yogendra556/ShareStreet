package com.example.sharestreet.domainLayer.model

import com.example.sharestreet.utils.RelationStatus

data class UserSearchResult(
    val user : UserModel,
    val status : RelationStatus
)