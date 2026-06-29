package com.example.sharestreet.dataLayer.Remote.Firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class FriendRemoteSource @Inject constructor(
    private val db : FirebaseFirestore
) {

    fun getFriendsList(UserId:String):{
        db.collection("Friends")
            .
    }
}