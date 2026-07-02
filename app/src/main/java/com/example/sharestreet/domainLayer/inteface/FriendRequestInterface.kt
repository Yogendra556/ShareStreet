package com.example.sharestreet.domainLayer.inteface

import com.example.sharestreet.domainLayer.model.FriendRequestModel

interface FriendRequestInterface {

    suspend fun addRequest(request: FriendRequestModel)
    suspend fun getReceiverRequestById(receiverId:String):List<FriendRequestModel>?
    suspend fun getSenderRequestById(senderId:String):List<FriendRequestModel>?
}