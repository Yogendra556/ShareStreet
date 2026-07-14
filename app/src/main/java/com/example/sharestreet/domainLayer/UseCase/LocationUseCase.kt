package com.example.sharestreet.domainLayer.UseCase

import androidx.compose.runtime.collectAsState
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.inteface.FriendsRepostoryInterface
import com.example.sharestreet.domainLayer.inteface.LocationRepository
import com.example.sharestreet.domainLayer.model.FriendLocationModel
import com.example.sharestreet.presentation.Location.LocationAccessState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val authRepo : AuthRepository,
    private val friendRepo : FriendsRepostoryInterface
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
        return locationRepository.observerAllAllowedUsers(userID).map { AllowedUserMap ->
            coroutineScope {
                AllowedUserMap.map { (friendId, isAllowed) ->
                    async {
                        val friend = authRepo.getUserById(friendId)
                        LocationAccessState(
                            friend, isAllowed
                        )
                    }
                }.awaitAll()
            }
        }
    }

    fun getFriendsLocation(userID: String):Flow<List<FriendLocationModel>>{
        val friendList = friendRepo.getFriendList(userID)
        val result = friendList.map {it->
            it.mapNotNull {friendId->
                val allowed = locationRepository.isUserAllowed(friendId,userID).first()
                if(allowed){
                    val user = authRepo.getUserById(userID)
                    val location = locationRepository.getLocation(friendId).first()
                    FriendLocationModel(user?.displayName,location.first,location.second)
                }
                else {
                    return@mapNotNull null
                }
            }
        }
        return result
    }
}