package com.example.sharestreet.ViewModels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharestreet.domainLayer.UseCase.AuthUseCase
import com.example.sharestreet.domainLayer.UseCase.FriendsUseCase
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
    private val authUseCas : AuthUseCase,
    private val friendUseCase : FriendsUseCase
): ViewModel(){

    private val _sentRequest = MutableStateFlow<List<UserModel?>>(emptyList())
    val sentRequest = _sentRequest.asStateFlow()
    private val _receivedRequest = MutableStateFlow<List<RequestWithUser>>(emptyList())
    val receivedRequest = _receivedRequest.asStateFlow()

    private val _searchUsers = MutableStateFlow<List<UserSearchResult>>(emptyList())
    val searchedUsers = _searchUsers.asStateFlow()

    fun addRequest(senderId:String,receiverId:String){
        viewModelScope.launch {
            requestUseCase.addRequest(senderId,receiverId)
        }
    }

    fun getSentRequest(senderId: String){
        viewModelScope.launch {
            requestUseCase.getSentRequest(senderId).collect {result->
                _sentRequest.value = result
            }
        }
    }

    fun getFriendRequest(receiverId: String){
        viewModelScope.launch {
           requestUseCase.getFriendRequest(receiverId).collect{
               _receivedRequest.value = it
           }
        }
    }


    fun searchUsers(senderId:String,searchName: String) {
        viewModelScope.launch {

            requestUseCase.searchUsersWithStatus(senderId, searchName).collect {
                _searchUsers.value = it


            }
        }
    }

    fun acceptRejectReq(requestId: String, type: String) {
        viewModelScope.launch {
            try {
                requestUseCase.acceptRejectRequest(requestId, type)
            } catch (e: Exception) {

            }
        }
    }
}