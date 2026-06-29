package com.example.sharestreet.domainLayer.UseCase

import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.model.UserModel
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    
    suspend fun signIn(email: String,password: String): UserModel{
        return authRepository.signIn(email,password)
    }

    suspend fun signUp(email: String,password: String): UserModel{
        return authRepository.signUp(email,password)
    }

    fun signOut() = authRepository.signOut()

    fun getCurrentUser(): UserModel?{
        return authRepository.getCurrentUser()
    }
}