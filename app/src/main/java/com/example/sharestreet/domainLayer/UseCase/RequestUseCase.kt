package com.example.sharestreet.domainLayer.UseCase

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.inteface.FriendRequestInterface
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.domainLayer.model.UserModel
import javax.inject.Inject

class RequestUseCase @Inject constructor(
    private val requestRepo: FriendRequestInterface,
    private val authRepo : AuthRepository
) {

    suspend fun addRequest(senderId: String,receiverId: String){
        if(senderId!=null && receiverId!=null) {
            val request = FriendRequestModel(
                senderId,
                receiverId,
                "Pending"
            )
            requestRepo.addRequest(request)
        }
        else{
            Log.d("ADD Request Failed sender or receiver is null","${senderId}+k+${receiverId}")
        }
    }

    suspend fun getFriendRequest(receiverId: String): List<UserModel?>?{
        val friendRequestList = requestRepo.getReceiverRequestById(receiverId)
        val friendsRequestsData = friendRequestList?.map {
            authRepo.getUserById(it.senderId)
        }
        return friendsRequestsData
    }

    suspend fun getSentRequest(senderId:String):  List<UserModel?>?{
        val sentRequestList = requestRepo.getSenderRequestById(senderId)
        val sentRequestsData = sentRequestList?.map {
            authRepo.getUserById(it.senderId)
        }
        return sentRequestsData
    }
}