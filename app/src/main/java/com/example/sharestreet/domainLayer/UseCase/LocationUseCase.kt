package com.example.sharestreet.domainLayer.UseCase

import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.inteface.LocationRepository
import com.example.sharestreet.presentation.Location.LocationAccessState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val authRepo : AuthRepository,
) {
    suspend fun updateLocation(lat:Double,long:Double){
            val user = authRepo.getCurrentUser()
            if(user!=null) locationRepository.updateLocation(user.uid,lat,long)
    }
    fun getLocation(userID: String): Flow<Pair<Double, Double>>{
        return locationRepository.getLocation(userID)
    }
    suspend fun addAllowedUser(userID: String,friendId:String){
        locationRepository.addAllowedUsers(userID,friendId)
    }
    suspend fun removeAllowedUser(userID: String,friendId: String){
        locationRepository.removeAllowedUser(userID,friendId)
    }
    suspend fun isUserAllowed(userID: String,friendId: String):Flow<Boolean>{
        return locationRepository.isUserAllowed(userID,friendId)
    }
    fun getAllowedUserList(userID: String): Flow<List<LocationAccessState>>{
        return locationRepository.observerAllAllowedUsers(userID).map{AllowedUserMap->
            AllowedUserMap.map{(friendId,isAllowed)->
                val friend = authRepo.getUserById(friendId)
                LocationAccessState(
                     friend,isAllowed
                )
            }
        }
    }
}