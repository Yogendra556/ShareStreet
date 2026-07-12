package com.example.sharestreet.dataLayer.Remote.Firebase

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.FriendRequestDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
    fun getReceiverRequestById(userId: String): Flow<List<FriendRequestDto>> =
        callbackFlow{
            val listener = db.collection("FriendRequest")
                .whereEqualTo("receiverId",userId)
                .whereEqualTo("status","Pending")
                .addSnapshotListener {snapshot,error->
                    if(error!=null){
                        close(error)
                        return@addSnapshotListener
                    }
                    val requests = snapshot
                        ?.toObjects(FriendRequestDto::class.java)?:emptyList()
                    trySend(requests)
                    Log.d("FriendRequest2","$requests")
                }
            awaitClose {
                listener.remove()
            }
        }


    // This fetches friendRequests for sender they can delete the request
    fun getSenderRequestById(senderId:String):Flow<List<FriendRequestDto>>{
        return callbackFlow {
            val listener = db.collection("FriendRequest")
                .whereEqualTo("senderId",senderId)
                .whereEqualTo("status","Pending")
                .addSnapshotListener {snapshot,error->
                    if(error!=null){
                        close(error)
                        return@addSnapshotListener
                    }
                    val requets = snapshot
                        ?.toObjects(FriendRequestDto::class.java)
                        ?:emptyList()
                    trySend(requets)
                }
            awaitClose {
                listener.remove()
            }
        }
    }
    // Type defines accept/reject
    suspend fun acceptRejectReq(requestId:String,type: String){
        val dbRef = db.collection("FriendRequest")
            .document(requestId)
            .update("status",type)
            .await()
    }
}