package com.example.sharestreet.domainLayer.inteface

import com.example.sharestreet.presentation.Location.LocationAccessState
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun updateLocation(userId:String,lat:Double,long:Double)
    fun getLocation(userId: String): Flow<Pair<Double,Double>>

    suspend fun addAllowedUsers(userId: String,friendId:String)

    suspend fun removeAllowedUser(userId: String,friendId:String)

    suspend fun isUserAllowed(userId: String,friendId: String): Flow<Boolean>

    fun observerAllAllowedUsers(userId: String): Flow<Map<String, Boolean>>
}