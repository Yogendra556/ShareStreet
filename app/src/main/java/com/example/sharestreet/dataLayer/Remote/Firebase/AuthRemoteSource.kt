package com.example.sharestreet.dataLayer.Remote.Firebase

import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private fun FirebaseUser.toDto():UserDto {
    return UserDto(
        uid = this.uid,
        email = this.email,
        displayName = this.displayName,
        isVerified = this.isEmailVerified
    )
}
class AuthRemoteSource @Inject constructor(
    private val firebaseAuth : FirebaseAuth
){
    suspend fun signIn(email:String,password:String): UserDto{
        val result = firebaseAuth
            .signInWithEmailAndPassword(email,password)
            .await()
        return result.user!!.toDto()
    }

    suspend fun signUp(email: String,password: String): UserDto{
        val result = firebaseAuth
            .createUserWithEmailAndPassword(email,password)
            .await()
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