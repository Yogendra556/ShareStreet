package com.example.sharestreet.domainLayer.UseCase

import android.util.Log
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.model.UserModel
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    
    suspend fun signIn(email: String,password: String): UserModel{
        return authRepository.signIn(email, password)
    }

    suspend fun signUp(displayName:String,email: String,password: String): UserModel{
        return authRepository.signUp(displayName,email,password)
    }

    fun signOut() = authRepository.signOut()

    fun getCurrentUser(): UserModel?{
        return authRepository.getCurrentUser()
    }

    suspend fun getUserById(userId:String): UserModel?{
        return authRepository.getUserById(userId)
    }

    suspend fun getUserByName(username:String): UserModel?{
        if(username!=null){
            return authRepository.getUserByName(username)
        }
        return null
    }

}