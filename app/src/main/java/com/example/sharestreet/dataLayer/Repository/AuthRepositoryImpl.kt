package com.example.sharestreet.dataLayer.Repository

import android.util.Log
import com.example.sharestreet.dataLayer.Remote.DTO.UserDto
import com.example.sharestreet.dataLayer.Remote.Firebase.AuthRemoteSource
import com.example.sharestreet.dataLayer.Remote.Firebase.UserRemoteSource
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.model.UserModel
import com.example.sharestreet.utils.toUserModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteSource: AuthRemoteSource,
    private val userRemoteSource: UserRemoteSource
): AuthRepository{

    override suspend fun signIn(email:String,password:String): UserModel{
        return authRemoteSource.signIn(email,password).toUserModel()
    }

    override suspend fun signUp(displayName:String,email: String,password: String): UserModel{
        val fireBaseUser = authRemoteSource.signUp(email,password)
        val user = UserDto(
            uid = fireBaseUser.uid,
            email = fireBaseUser.email,
            displayName = displayName,
            isVerified = fireBaseUser.isVerified
        )
        Log.d("FireBaseUser","${fireBaseUser}")
        Log.d("Add User","${user}")

        userRemoteSource.addUser(user)
        return fireBaseUser.toUserModel()
    }

    override fun signOut(){
        authRemoteSource.signOut()
    }

    override fun getCurrentUser(): UserModel?{
        return authRemoteSource.getCurrentUser()?.toUserModel()
    }

    override suspend fun getUserById(UserId: String): UserModel?{
        return userRemoteSource.getUserById(UserId)?.toUserModel()
    }

    override suspend fun getUserByName(username:String):UserModel?{
        return userRemoteSource.getUserByName(username)?.toUserModel()
    }
}