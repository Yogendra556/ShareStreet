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

    fun signIn(email:String,password:String){
        viewModelScope.launch {
            _user.value = authUseCase.signIn(email,password)
        }
    }

    fun signUp(email:String,password: String){
        viewModelScope.launch {
            _user.value = authUseCase.signUp(email,password)
        }
    }

    fun signOut() = authUseCase.signOut()

    fun getCurrentUser(): UserModel?{
        return authUseCase.getCurrentUser()
    }
}