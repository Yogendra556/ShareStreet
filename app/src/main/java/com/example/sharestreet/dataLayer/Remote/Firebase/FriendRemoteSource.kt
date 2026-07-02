package com.example.sharestreet.dataLayer.Remote.Firebase

import com.example.sharestreet.dataLayer.Remote.DTO.FriendsDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRemoteSource @Inject constructor(
    private val db : FirebaseFirestore
) {

    // For friends collection the id is same as that user's id

    suspend fun addFriend(userId: String, friendsId: String){
        db.collection("Friends")
            .document(userId)
            .set(
                mapOf(
                    "friendsList" to FieldValue.arrayUnion(friendsId)
                ),
                SetOptions.merge()
            )
    }

    suspend fun removeFriend(userId: String, friendsId: String){
        db.collection("Friends")
            .document(userId)
            .set(
                mapOf(
                    "friendsList" to FieldValue.arrayRemove(friendsId)
                ),
                SetOptions.merge()
            )
    }

    suspend fun getFriendsList(userId: String):List<String>?{
        val list = db.collection("Friends")
            .document(userId)
            .get()
            .await()
        return list.toObject(FriendsDto::class.java)?.friendsList
    }
}