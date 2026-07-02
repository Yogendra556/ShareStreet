package com.example.sharestreet.dataLayer.Repository

import com.example.sharestreet.dataLayer.Remote.Firebase.FriendRequestRemoteSource
import com.example.sharestreet.domainLayer.inteface.FriendRequestInterface
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.utils.toFriendRequestDto
import com.example.sharestreet.utils.toFriendRequestModel
import javax.inject.Inject

class FriendRequestRepoImpl @Inject constructor(
    private val remoteSource: FriendRequestRemoteSource
): FriendRequestInterface{

    override suspend fun addRequest(request: FriendRequestModel){
        val requestDto = request.toFriendRequestDto()
        remoteSource.addRequest(requestDto)
    }

    override suspend fun getReceiverRequestById(receiverId:String):List<FriendRequestModel>?{
        val result = remoteSource.getRecieverRequestById(receiverId)?.map{
            it.toFriendRequestModel()
        }
        return result
    }

    override suspend fun getSenderRequestById(senderId:String): List<FriendRequestModel>?{
        val result = remoteSource.getSenderRequestById(senderId)?.map {
            it.toFriendRequestModel()
        }
        return result
    }
}