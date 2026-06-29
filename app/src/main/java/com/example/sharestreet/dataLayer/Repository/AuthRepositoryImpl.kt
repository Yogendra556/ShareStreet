package com.example.sharestreet.dataLayer.Repository

import com.example.sharestreet.dataLayer.Remote.Firebase.AuthRemoteSource
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.model.UserModel
import com.example.sharestreet.utils.toUserModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteSource: AuthRemoteSource
): AuthRepository{

    override suspend fun signIn(email:String,password:String): UserModel{
        return authRemoteSource.signIn(email,password).toUserModel()
    }

    override suspend fun signUp(email: String,password: String): UserModel{
        return authRemoteSource.signUp(email,password).toUserModel()
    }

    override fun signOut(){
        authRemoteSource.signOut()
    }

    override fun getCurrentUser(): UserModel?{
        return authRemoteSource.getCurrentUser()?.toUserModel()
    }
}