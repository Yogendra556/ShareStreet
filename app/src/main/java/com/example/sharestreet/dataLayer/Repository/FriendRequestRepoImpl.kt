package com.example.sharestreet.dataLayer.Repository

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.FriendRequestDto
import com.example.sharestreet.dataLayer.Remote.Firebase.FriendRequestRemoteSource
import com.example.sharestreet.domainLayer.inteface.FriendRequestInterface
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.utils.toFriendRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FriendRequestRepoImpl @Inject constructor(
    private val remoteSource: FriendRequestRemoteSource
): FriendRequestInterface{

    override suspend fun addRequest(senderId: String,receiverId: String){
        remoteSource.addRequest(senderId,receiverId)
    }

    // We can map flow firstly to the list then trnasform the list from dto to model
    override fun getReceiverRequestById(receiverId:String):Flow<List<FriendRequestModel>>{
        val result = remoteSource.getReceiverRequestById(receiverId).map { requestlist->
            requestlist.map {
                it.toFriendRequestModel()
            }
        }
        Log.d("FriendRequest","$result")
        return result
    }

    override fun getSenderRequestById(senderId:String): Flow<List<FriendRequestModel>>{
        val result = remoteSource.getSenderRequestById(senderId).map {requestList->
            requestList.map {
                it.toFriendRequestModel()
            }
        }
        return result
    }

    override suspend fun acceptRejectRequest(requestId: String, type: String) {
        remoteSource.acceptRejectReq(requestId,type)
    }

    override suspend fun getRequestById(reqId:String): FriendRequestDto?{
        return remoteSource.getRequestById(reqId)
    }
}