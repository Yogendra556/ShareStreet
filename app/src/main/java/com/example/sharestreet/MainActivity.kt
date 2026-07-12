package com.example.sharestreet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sharestreet.ViewModels.AuthViewModel
import com.example.sharestreet.presentation.Auth.SignUpScreen
import com.example.sharestreet.presentation.Auth.signInPage
import com.example.sharestreet.presentation.Friends.friendsScreen
import com.example.sharestreet.presentation.Home.Homescreen
import com.example.sharestreet.presentation.Location.AllowedFriendsScreen
import com.example.sharestreet.ui.theme.ShareStreetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Use viewModels when use in activity
    // AuthViewModel = hiltViewModel() can be used only in composables not in activity

    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShareStreetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if(authViewModel.getCurrentUser()==null) "SignIn" else "Home",
                    ){
                        composable("SignIn"){
                            signInPage(navController)
                        }
                        composable("SignUp"){
                            SignUpScreen(navController)
                        }
                        composable("Home"){
                            Homescreen(navController)
                        }
                        composable("FriendScreen"){
                            friendsScreen()
                        }

                        composable("Location"){
                            AllowedFriendsScreen()
                        }
                    }
                }
            }
        }
    }
}
