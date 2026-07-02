package com.example.sharestreet.domainLayer.inteface

import com.example.sharestreet.dataLayer.Repository.AuthRepositoryImpl
import com.example.sharestreet.domainLayer.model.UserModel
import javax.inject.Inject

 interface AuthRepository {
    suspend fun signIn(email: String, password: String): UserModel
    suspend fun signUp(displayName:String,email: String, password: String): UserModel
    fun signOut()
    fun getCurrentUser(): UserModel?
    suspend fun getUserById(userId: String): UserModel?

     suspend fun getUserByName(username:String): UserModel?
}