package com.example.sharestreet.domainLayer.UseCase

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.FriendRequestDto
import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.inteface.FriendRequestInterface
import com.example.sharestreet.domainLayer.inteface.FriendsRepostoryInterface
import com.example.sharestreet.domainLayer.inteface.LocationRepository
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.domainLayer.model.RequestWithUser
import com.example.sharestreet.domainLayer.model.UserModel
import com.example.sharestreet.domainLayer.model.UserSearchResult
import com.example.sharestreet.utils.RelationStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RequestUseCase @Inject constructor(
    private val requestRepo: FriendRequestInterface,
    private val authRepo : AuthRepository,
    private val friendRepo : FriendsRepostoryInterface,
    private val locationRepo: LocationRepository
) {

    suspend fun addRequest(senderId: String,receiverId: String){
        if(senderId!=null && receiverId!=null) {
            requestRepo.addRequest(senderId,receiverId)
        }
        else{
            Log.d("ADD Request Failed sender or receiver is null","${senderId}+k+${receiverId}")
        }
    }

    fun getFriendRequest(receiverId: String): Flow<List<RequestWithUser>>{
        val friendRequestList = requestRepo.getReceiverRequestById(receiverId)
        val friendsRequestsData = friendRequestList.map { list ->
            list.map {
                val user = authRepo.getUserById(it.senderId)
                val requestId = it.reqestUid
                RequestWithUser(user!!, requestId)
            }
        }
        return friendsRequestsData
    }

    fun getSentRequest(senderId:String):  Flow<List<UserModel?>>{
        val sentRequestList = requestRepo.getSenderRequestById(senderId)
        val sentRequestsData = sentRequestList.map { list ->
            coroutineScope {
                list.map {
                    async {
                        authRepo.getUserById(it.senderId)
                    }
                }.awaitAll()
            }
        }
        return sentRequestsData
    }

    suspend fun searchUsersWithStatus(senderId: String,searchName:String):Flow<List<UserSearchResult>>{
        val searchedUsers = authRepo.getUserByName(searchName)?:emptyList()
        val sentRequestFlow = requestRepo.getSenderRequestById(senderId)
        //First map flow so if flow update we recalculate status
        Log.d("SEARCH_DEBUG", "searchedUsers = $searchedUsers")
        val result = sentRequestFlow.map { sentRequestList ->
            Log.d(
                "SEARCH_DEBUG1",
                "sentRequestList = $sentRequestList"
            )

            searchedUsers.map { user ->
                val status = when {
                    sentRequestList.any { req ->
                        req.receiverId == user.uid &&
                                req.status == "Pending"
                    } -> RelationStatus.Sent

                    sentRequestList.any { req ->
                        req.receiverId == user.uid &&
                                req.status == "Accepted"
                    } -> RelationStatus.Friends

                    else -> RelationStatus.None
                }
                UserSearchResult(
                    user = user,
                    status = status
                )
            }
        }
            return result
    }
    suspend fun getRequestById(requestId: String): FriendRequestDto?{
        return requestRepo.getRequestById(requestId)
    }
    suspend fun acceptRejectRequest(requestId:String,type:String){
        val request = getRequestById(requestId)
        if(request!=null && type=="Accept"){
            val senderId = request.senderId
            val receiverId = request.receiverId
            friendRepo.addFriend(senderId,receiverId)
            locationRepo.addAllowedUsers(senderId,receiverId)
        }
        requestRepo.acceptRejectRequest(requestId,type)
    }
}