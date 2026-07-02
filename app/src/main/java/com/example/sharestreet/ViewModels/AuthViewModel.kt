package com.example.sharestreet.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharestreet.domainLayer.UseCase.AuthUseCase
import com.example.sharestreet.domainLayer.inteface.AuthRepository
import com.example.sharestreet.domainLayer.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    private val _userById = MutableStateFlow<UserModel?>(null)
    val userById = _userById.asStateFlow()

    private val _userByName = MutableStateFlow<UserModel?>(null)
    val userByName = _userByName.asStateFlow()

    fun signIn(email:String,password:String){
        viewModelScope.launch {
            _user.value = authUseCase.signIn(email,password)
        }
    }

    fun signUp(displayName:String,email:String,password: String){
        viewModelScope.launch {
            _user.value = authUseCase.signUp(displayName,email,password)
        }
    }

    fun signOut() = authUseCase.signOut()

    fun getCurrentUser(): UserModel?{
        return authUseCase.getCurrentUser()
    }

    fun getUserById(userId:String){
        viewModelScope.launch {
            _userById.value = authUseCase.getUserById(userId)
        }
    }

    fun getUserByName(username:String) {
        if (username != null) {
            viewModelScope.launch {
                _userByName.value = authUseCase.getUserByName(username)
            }
        }
    }

}