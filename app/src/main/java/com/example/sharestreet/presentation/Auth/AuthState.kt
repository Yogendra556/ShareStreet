package com.example.sharestreet.presentation.Auth

sealed class AuthState {
    object Idle: AuthState()
    object Success:AuthState()
    object Loading:AuthState()
    data class Error(val msg:String): AuthState()
}