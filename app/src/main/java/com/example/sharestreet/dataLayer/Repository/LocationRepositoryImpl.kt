package com.example.sharestreet.dataLayer.Repository

import com.example.sharestreet.dataLayer.Remote.Firebase.LocationRemoteSource
import com.example.sharestreet.domainLayer.inteface.LocationRepository
import com.example.sharestreet.presentation.Location.LocationAccessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val remoteSource: LocationRemoteSource
): LocationRepository {

    override suspend fun updateLocation(userId:String,lat: Double,long: Double){
        remoteSource.updateLocation(userId,lat,long)
    }

    override fun getLocation(userId: String): Flow<Pair<Double,Double>>{
        return remoteSource.observeLocation(userId)
    }

    override suspend fun addAllowedUsers(userId: String, friendId: String) {
        remoteSource.addAllowedUsers(userId,friendId)
    }

    override suspend fun removeAllowedUser(userId: String, friendId: String) {
        remoteSource.removeAllowedUsers(userId,friendId)
    }

    override suspend fun isUserAllowed(
        userId: String,
        friendId: String
    ): Flow<Boolean> {
        return remoteSource.isUserAllowed(userId,friendId)
    }

    override fun observerAllAllowedUsers(userId: String): Flow<Map<String,Boolean>> {
        return remoteSource.observeAllAllowedUsers(userId)
    }
}