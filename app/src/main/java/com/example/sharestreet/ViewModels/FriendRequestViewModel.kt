package com.example.sharestreet.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharestreet.domainLayer.UseCase.AuthUseCase
import com.example.sharestreet.domainLayer.UseCase.RequestUseCase
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.domainLayer.model.RequestWithUser
import com.example.sharestreet.domainLayer.model.UserModel
import com.example.sharestreet.domainLayer.model.UserSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val requestUseCase: RequestUseCase,
    private val authUseCas : AuthUseCase
): ViewModel(){

    private val _sentRequest = MutableStateFlow<List<UserModel?>?>(null)
    val sentRequest = _sentRequest.asStateFlow()
    private val _receivedRequest = MutableStateFlow<List<RequestWithUser>?>(null)
    val receivedRequest = _receivedRequest.asStateFlow()

    private val _searchUsers = MutableStateFlow<List<UserSearchResult>?>(null)
    val searchedUsers = _searchUsers.asStateFlow()

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


    fun searchUsers(senderId:String,searchName: String){
        viewModelScope.launch {
            _searchUsers.value = requestUseCase.searchUsersWithStatus(senderId, searchName)
        }
    }

    fun acceptRejectReq(requestId:String,type: String){
        viewModelScope.launch {
            requestUseCase.acceptRejectRequest(requestId,type)
        }
    }
}