package com.example.sharestreet.ViewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharestreet.domainLayer.UseCase.LocationUseCase
import com.example.sharestreet.domainLayer.model.FriendLocationModel
import com.example.sharestreet.presentation.Location.LocationAccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val useCase: LocationUseCase
) : ViewModel(){

    private val _isAllowed = MutableStateFlow(false)
    val isAllowed = _isAllowed.asStateFlow()

    private val _AllowedUsersList = MutableStateFlow<List<LocationAccessState>>(emptyList())
    val allowedUsersList = _AllowedUsersList.asStateFlow()

    private val _friendsLocation = MutableStateFlow<List<FriendLocationModel>>(emptyList())
    val friendsLocation = _friendsLocation.asStateFlow()
    fun addAllowedUsers(userId:String,friendId:String){
        viewModelScope.launch {
            useCase.addAllowedUser(userId,friendId)
        }
    }

    fun removeAllowedUsers(userId: String,friendId: String){
        viewModelScope.launch {
            useCase.removeAllowedUser(userId,friendId)
        }
    }

    fun isUserAllowed(userId: String,friendId: String){
        viewModelScope.launch {
            useCase.isUserAllowed(userId,friendId).collect {it->
                _isAllowed.value = it
            }
        }
    }

    fun observeAllAllowedUsers(userId: String){
        viewModelScope.launch {
            useCase.getAllowedUserList(userId).collect{it->
                _AllowedUsersList.value = it
            }
        }
    }

    fun getFriendsLocation(userId: String){
        viewModelScope.launch {
            useCase.getFriendsLocation(userId).collect {
                _friendsLocation.value = it
            }
        }
    }
}