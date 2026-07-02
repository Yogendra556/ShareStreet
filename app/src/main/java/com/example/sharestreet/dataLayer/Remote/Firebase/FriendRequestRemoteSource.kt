package com.example.sharestreet.dataLayer.Remote.Firebase

import com.example.sharestreet.dataLayer.Remote.DTO.FriendRequestDto
import com.example.sharestreet.dataLayer.Remote.DTO.FriendsDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRequestRemoteSource @Inject constructor(
    private val db : FirebaseFirestore
) {

    suspend fun addRequest(request: FriendRequestDto){
        db.collection("FriendRequest")
            .add(request)
            .await()
    }
    // This fetches friendrequests for receiver they can accept or reject
    suspend fun getRecieverRequestById(userId: String):List<FriendRequestDto>?{
         val result = db.collection("FriendRequest")
            .whereEqualTo("ReceiverId",userId)
            .whereEqualTo("Status","Pending")
            .get()
            .await()
        return result.toObjects(FriendRequestDto::class.java)
    }
    // This fetches friendRequests for sender they can delete the request
    suspend fun getSenderRequestById(senderId:String):List<FriendRequestDto>?{
        val result = db.collection("FriendRequest")
            .whereEqualTo("SenderId",senderId)
            .whereEqualTo("Status","Pending")
            .get()
            .await()
        return result.toObjects(FriendRequestDto::class.java)
    }

}