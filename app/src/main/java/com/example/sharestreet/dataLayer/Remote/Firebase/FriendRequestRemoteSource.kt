package com.example.sharestreet.dataLayer.Remote.Firebase

import android.R
import com.example.sharestreet.dataLayer.Remote.DTO.FriendRequestDto
import com.example.sharestreet.dataLayer.Remote.DTO.FriendsDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRequestRemoteSource @Inject constructor(
    private val db : FirebaseFirestore
) {
    suspend fun addRequest(senderId: String,receiverId:String){
        val docRef = db.collection("FriendRequest")
            .document()
        val request = FriendRequestDto(
            uid = docRef.id,
            senderId = senderId,
            receiverId = receiverId,
            status = "Pending"
        )
        docRef.set(request)
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
    // Type defines accept/reject
    suspend fun acceptRejectReq(requestId:String,type: String){
        val dbRef = db.collection("FriendRequest")
            .document(requestId)
            .update("status",type)
            .await()
    }

}