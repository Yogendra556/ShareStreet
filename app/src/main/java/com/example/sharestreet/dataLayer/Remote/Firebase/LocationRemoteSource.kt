package com.example.sharestreet.dataLayer.Remote.Firebase

import com.example.sharestreet.dataLayer.Remote.DTO.LocationDTO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firestore.v1.Value
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRemoteSource @Inject constructor(
    private val rtdb : FirebaseDatabase
) {
    suspend fun updateLocation(userId:String,lat: Double,long: Double){
        val ref = rtdb.getReference("Location/${userId}")
        val locationData = mapOf(
            "lat" to lat,
            "long" to long
        )
        ref.setValue(locationData).await()
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

    suspend fun addAllowedUsers(userId: String,friendId: String){
        val ref = rtdb.getReference("Location/${userId}/AllowedUsers/${friendId}")
        ref.setValue(true).await()
    }

    suspend fun removeAllowedUsers(userId: String,friendId: String){
        val ref = rtdb.getReference("Location/${userId}/AllowedUsers/${friendId}")
        ref.setValue(false).await()
    }

    fun isUserAllowed(userId: String,friendId: String):Flow<Boolean> {
        return callbackFlow {
            val ref = rtdb.getReference("Location/${userId}/AllowedUse/${friendId}")
            val listener = object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.getValue(Boolean::class.java)?:false
                    trySend(result)
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

    fun observeAllAllowedUsers(userId: String):Flow<Map<String,Boolean>>{
        return callbackFlow {
            val ref = rtdb.getReference("Location/${userId}/AllowedUsers")
            val listener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val map = snapshot.children.associate{child->
                        val friendsId = child.key.orEmpty()
                        val allowedUser = child.getValue(Boolean::class.java)?:false
                        friendsId to allowedUser
                    }
                    trySend(map)
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
}