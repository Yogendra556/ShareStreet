package com.example.sharestreet.presentation.Location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sharestreet.ViewModels.AuthViewModel
import com.example.sharestreet.ViewModels.FriendViewModel
import com.example.sharestreet.ViewModels.LocationViewModel
import com.example.sharestreet.domainLayer.model.UserModel

@Composable
fun AllowedFriendsScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    friendViewModel: FriendViewModel = hiltViewModel()
){
    val currentUser = authViewModel.getCurrentUser()?.uid
    Text("Decide who can see your location")
    val friendsList by friendViewModel.friendsList.collectAsStateWithLifecycle()
    LaunchedEffect(currentUser){
        currentUser?.let {
            friendViewModel.getFriendsList(currentUser)
        }
    }
    if(currentUser!=null) usersList(currentUser,addAllowedUser={friendId->
        locationViewModel.addAllowedUsers(currentUser,friendId)
    },removeAllowedUser={friendId->
        locationViewModel.removeAllowedUsers(currentUser, friendId)
    },friendsList)
}
@Composable
fun usersList(
    currentUser : String,
    addAllowedUser : (String)->Unit={},
    removeAllowedUser:(String)->Unit={},
    friendsList:List<UserModel?>
){
    LazyColumn() {items(friendsList.size){idx->
         if(friendsList[idx]!=null) {
           userListCard(friendsList[idx]!!,addAllowedUser,removeAllowedUser)
         }
       }
    }
}

@Composable
fun userListCard(
    friend : UserModel,
    addAllowedUser : (String)->Unit={},
    removeAllowedUser:(String)->Unit={},
){
    Box(){
        Text(friend.displayName)
        Button() {
            Text("Allow")
        }
        Button() {
            Text("Dont Allow")
        }
    }
}