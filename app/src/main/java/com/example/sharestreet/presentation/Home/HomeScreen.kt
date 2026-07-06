package com.example.sharestreet.presentation.Home

import android.R.attr.navigationIcon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharestreet.ViewModels.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.sharestreet.ViewModels.FriendRequestViewModel
import com.example.sharestreet.domainLayer.model.FriendRequestModel
import com.example.sharestreet.domainLayer.model.UserModel
import com.example.sharestreet.domainLayer.model.UserSearchResult
import com.example.sharestreet.utils.RelationStatus
import kotlinx.coroutines.launch

@Composable
fun Homescreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    friendsRequestViewModel: FriendRequestViewModel = hiltViewModel()
){
    var searchValue by remember {
        mutableStateOf("")
    }
    val currentUser = authViewModel.getCurrentUser()
    fun searchFun(){
        friendsRequestViewModel.searchUsers(currentUser!!.uid,searchValue)
    }
    val searchResult by friendsRequestViewModel.searchedUsers.collectAsState()
    Log.d("SearchResult","${searchResult}")


    sideDrawer(
        searchValue = searchValue,
        onValueChange = { searchValue = it },
        searchFun = { searchFun() },
        navController = navController
    ) {
        if (currentUser != null && searchResult != null) {
            SearchResultList(
                userFound = searchResult,
                senderId = currentUser.uid
            )
        }
    }
}
@Composable
fun SearchResultList(
    userFound: List<UserSearchResult>?,
    friendsRequestViewModel: FriendRequestViewModel = hiltViewModel(),
    senderId : String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(userFound!!.size) { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userFound[user].user.displayName ?: "Unknown User",
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            friendsRequestViewModel.addRequest(senderId,userFound[user].user.uid)
                        }
                    ) {
                        val userStatus = userFound[user].status
                        if(userStatus == RelationStatus.None) Text("Add")
                        else if(userStatus == RelationStatus.Friends){
                           Text("Friends")
                        }
                        else if(userStatus == RelationStatus.Sent){
                            Text("Pending")
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sideDrawer(
    searchValue: String,
    onValueChange: (String)-> Unit,
    searchFun: ()->Unit = {},
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    content:@Composable ()-> Unit
){

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet{
                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = {Text("Friends")},
                    selected = false,
                    onClick = {navController.navigate("FriendScreen")}
                )
                NavigationDrawerItem(
                    label = {Text("Location")},
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = {Text("Log Out")},
                    selected = false,
                    onClick = {
                        authViewModel.signOut()
                        navController.navigate("Home")
                    }
                )
            }
        }
    ){Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                title = {
                    TextField(
                        value = searchValue,
                        onValueChange = {
                            onValueChange(it)
                        },
                        placeholder = { Text("Search") }
                    )
                },
                actions = {
                    IconButton(onClick = {
                        searchFun()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) {paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }

    }
}

@Preview(showBackground = true)
@Composable
fun previewFun(){

}