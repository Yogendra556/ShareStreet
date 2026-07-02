package com.example.sharestreet.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharestreet.domainLayer.UseCase.RequestUseCase
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.domainLayer.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val requestUseCase: RequestUseCase
): ViewModel(){

    private val _sentRequest = MutableStateFlow<List<UserModel?>?>(null)
    val sentRequest = _sentRequest.asStateFlow()
    private val _receivedRequest = MutableStateFlow<List<UserModel?>?>(null)
    val receivedRequest = _receivedRequest.asStateFlow()

    fun addRequest(senderId:String,receiverId:String){
        viewModelScope.launch {
            requestUseCase.addRequest(senderId,receiverId)
        }
    }

    fun getSentRequest(senderId: String){
        viewModelScope.launch {
            _sentRequest.value = requestUseCase.getSentRequest(senderId)
        }
    }

    fun getFriendRequest(receiverId: String){
        viewModelScope.launch {
            _receivedRequest.value = requestUseCase.getFriendRequest(receiverId)
        }
    }
}