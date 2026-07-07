package com.example.sharestreet.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharestreet.domainLayer.UseCase.FriendsUseCase
import com.example.sharestreet.domainLayer.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendsUseCase: FriendsUseCase
): ViewModel() {

    private val _friendsList = MutableStateFlow<List<UserModel?>>(emptyList())
    val friendsList = _friendsList.asStateFlow()

    fun getFriendsList(UserId:String){
        viewModelScope.launch {
            friendsUseCase.getFriendList(UserId).collect {
                _friendsList.value = it
            }
        }
    }

    fun addFriend(UserId: String,friendId:String){
        viewModelScope.launch {
            friendsUseCase.addFriend(UserId,friendId)
        }
    }

    fun removeFriend(UserId: String,friendId: String){
        viewModelScope.launch {
            friendsUseCase.removeFriend(UserId,friendId)
        }
    }
}