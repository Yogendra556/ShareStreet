package com.example.sharestreet.dataLayer.Remote.Firebase

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.example.sharestreet.utils.toDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRemoteSource @Inject constructor(
    private val firebaseAuth : FirebaseAuth
){
    suspend fun signIn(email:String,password:String): UserDto{
        val result = firebaseAuth
            .signInWithEmailAndPassword(email,password)
            .await()
        Log.d("UserSignIN","${result.user!!.toDto().toString()}")
        return result.user!!.toDto()
    }

    suspend fun signUp(email: String,password: String): UserDto{
        val result = firebaseAuth
            .createUserWithEmailAndPassword(email,password)
            .await()
        Log.d("UserSignUp","${result.user!!.toDto().toString()}")
        return result.user!!.toDto()
    }

    fun signOut(){
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): UserDto?{
        val result = firebaseAuth.currentUser?.toDto()
        return result
    }

}


