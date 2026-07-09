package com.example.sharestreet.dataLayer.Remote.DTO

data class LocationDTO(
    val uid:String="",
    val lat:Double=0.0,
    val long:Double=0.0,
    val AllowedUsers:List<String>
)