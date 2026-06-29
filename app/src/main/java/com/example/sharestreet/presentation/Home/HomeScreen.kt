package com.example.sharestreet.presentation.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharestreet.ViewModels.AuthViewModel

@Composable
fun Homescreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
){
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(64.dp)
    ){
        Button(
            onClick = {
                authViewModel.signOut()
                navController.navigate("SignIn")
            },
            modifier = Modifier.fillMaxWidth()
                .height(28.dp)
                .background(color = Color.Red)
        ) {
            Text("SignOut")
        }

    }
}