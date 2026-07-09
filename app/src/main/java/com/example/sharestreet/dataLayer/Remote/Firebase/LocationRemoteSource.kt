package com.example.sharestreet.dataLayer.Remote.Firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firestore.v1.Value
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationRemoteSource @Inject constructor(
    private val rtdb : FirebaseDatabase
) {
    fun updateLocation(userId:String,lat: Double,long: Double){
        val ref = rtdb.getReference("Location/${userId}")
        val locationData = mapOf(
            "lat" to lat,
            "long" to long
        )
        ref.setValue(locationData)
    }

    fun observeLocation(userId: String): Flow<Pair<Double, Double>> =
        callbackFlow{
        val ref = rtdb.getReference("Location/${userId}")
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("lat").getValue(Double::class.java)?:0.0
                val long = snapshot.child("long").getValue(Double::class.java)?:0.0
                trySend(Pair(lat,long))
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
            ref.addValueEventListener(listener)
            awaitClose {
                ref.removeEventListener(listener)
            }
    }
}