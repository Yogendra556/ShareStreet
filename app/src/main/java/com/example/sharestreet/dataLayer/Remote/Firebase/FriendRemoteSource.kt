package com.example.sharestreet.dataLayer.Remote.Firebase

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.FriendsDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log

class FriendRemoteSource @Inject constructor(
    private val db : FirebaseFirestore
) {

    // For friends collection the id is same as that user's id
    // Batch is used to ensure both operations are performed simultaneously if one fails another one also doesnt continue
    suspend fun addFriend(userId: String, friendsId: String){
        val userRef = db.collection("Friends")
            .document(userId)
        val friendRef = db.collection("Friends")
            .document(friendsId)
        db.runBatch {batch ->
            batch.set(
                userRef,
                mapOf(
                    "friendsList" to FieldValue.arrayUnion(friendsId)
                ),
                SetOptions.merge()
            )
            batch.set(
                friendRef,
                mapOf(
                    "friendsList" to FieldValue.arrayUnion(userId)
                ),
                SetOptions.merge()
            )
        }.await()
    }

    suspend fun removeFriend(userId: String, friendsId: String){
        val userRef = db.collection("Friends")
                         .document(userId)
        val friendRef = db.collection("Friends")
                          .document(friendsId)
        db.runBatch {batch ->
            batch.set(
                userRef,
                mapOf(
                    "friendsList" to FieldValue.arrayRemove(friendsId)
                ),
                SetOptions.merge()
            )
            batch.set(
                friendRef,
                mapOf(
                    "friendsList" to FieldValue.arrayRemove(userId)
                ),
                SetOptions.merge()
            )
        }.await()
    }

    fun getFriendsList(userId: String): Flow<List<String>> =
         callbackFlow {
              val listener = db.collection("Friends")
                 .document(userId)
                 .addSnapshotListener {snapshot,error->
                     if(error!=null){
                         close(error)
                         return@addSnapshotListener
                     }
                     val list = snapshot?.toObject(FriendsDto::class.java)?.friendsList?:emptyList()
                     trySend(list)
                 }
             awaitClose {
                 listener.remove()
             }
         }
}
