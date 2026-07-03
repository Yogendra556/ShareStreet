package com.example.sharestreet.dataLayer.Remote.Firebase

import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun addUser(user: UserDto){
        db.collection("User")
            .document(user.uid)
            .set(user)
            .await()
    }
    suspend fun getUserById(userId:String): UserDto? {
        val item = db.collection("User")
            .document(userId)
            .get()
            .await()

        return item.toObject(UserDto::class.java)
    }

    suspend fun getUserByName(userName: String): List<UserDto>?{
        val item = db.collection("User")
            .get()
            .await()
            .toObjects(UserDto::class.java)

        return item.filter{
             it.displayName!!.contains(userName)
        }
    }
}