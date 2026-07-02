package com.example.sharestreet.presentation.Home

import android.R.attr.navigationIcon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.sharestreet.domainLayer.model.UserModel
import kotlinx.coroutines.launch

@Composable
fun Homescreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
){
    var searchValue by remember {
        mutableStateOf("")
    }
    fun searchFun(){
        authViewModel.getUserByName(searchValue)
    }
    val searchResult by authViewModel.userByName.collectAsState()
    Log.d("SearchResult","${searchResult}")
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column() {
            Box(){
                sideDrawer(searchValue, { searchValue = it }, { searchFun() })
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(12.dp)
            ){
                SearchResultCard(searchResult)
            }
        }
    }
}
@Composable
fun SearchResultCard(
    userFound: UserModel?
){
    if(userFound!=null){
        Card(
            modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("${userFound?.displayName}")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sideDrawer(
    searchValue: String,
    onValueChange: (String)-> Unit,
    searchFun: ()->Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
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
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = {Text("Location")},
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = {Text("Log Out")},
                    selected = false,
                    onClick = {authViewModel.signOut()}
                )
            }
        }
    ){
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
}