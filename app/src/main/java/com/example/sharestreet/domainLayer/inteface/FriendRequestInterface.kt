package com.example.sharestreet.domainLayer.inteface

import com.example.sharestreet.domainLayer.model.FriendRequestModel
import kotlinx.coroutines.flow.Flow

interface FriendRequestInterface {

    suspend fun addRequest(senderId: String,receiverId: String)
    fun getReceiverRequestById(receiverId:String): Flow<List<FriendRequestModel>>
    fun getSenderRequestById(senderId:String):Flow<List<FriendRequestModel>>

    suspend fun acceptRejectRequest(requestId:String,type:String)
}