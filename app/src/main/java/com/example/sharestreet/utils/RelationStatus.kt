package com.example.sharestreet.utils

sealed class RelationStatus {

    data object None : RelationStatus()
    data object Sent : RelationStatus()
    data class RequestReceived(
         val requestId : String
    ): RelationStatus()
    data object Friends : RelationStatus()
}