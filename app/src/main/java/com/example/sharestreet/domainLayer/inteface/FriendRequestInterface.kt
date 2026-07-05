package com.example.sharestreet.domainLayer.inteface

import com.example.sharestreet.domainLayer.model.FriendRequestModel

interface FriendRequestInterface {

    suspend fun addRequest(senderId: String,receiverId: String)
    suspend fun getReceiverRequestById(receiverId:String):List<FriendRequestModel>?
    suspend fun getSenderRequestById(senderId:String):List<FriendRequestModel>?

    suspend fun acceptRejectRequest(requestId:String,type:String)
}