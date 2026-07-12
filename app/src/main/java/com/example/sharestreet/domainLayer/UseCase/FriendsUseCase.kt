package com.example.sharestreet.domainLayer.UseCase

import android.location.Location
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.inteface.FriendsRepostoryInterface
import com.example.sharestreet.domainLayer.inteface.LocationRepository
import com.example.sharestreet.domainLayer.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FriendsUseCase @Inject constructor(
    private val friendsRepository : FriendsRepostoryInterface,
    private val authRepository: AuthRepository,
    private val locationRepository: LocationRepository
) {

    suspend fun addFriend(UserId:String,friendId:String) =
        coroutineScope{
        async{locationRepository.addAllowedUsers(UserId, friendId)}.await()
        async {friendsRepository.addFriend(UserId, friendId)}.await()
    }
    suspend fun removeFriend(UserId: String,friendId: String) {
            friendsRepository.removeFriend(UserId, friendId)
    }
    fun getFriendList(UserId: String): Flow<List<UserModel?>>{
        val friendIdList = friendsRepository.getFriendList(UserId)
        return friendIdList.map {list->
               list.map {it->
                   authRepository.getUserById(it)
               }
        }
    }
}