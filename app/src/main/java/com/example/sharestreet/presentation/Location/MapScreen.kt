package com.example.sharestreet.presentation.Location

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.sharestreet.ViewModels.LocationViewModel
import com.example.sharestreet.domainLayer.model.FriendLocationModel
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import kotlin.collections.forEach
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun MapScreen(
    friendsLocation : List<FriendLocationModel>,
    locationViewModel : LocationViewModel = hiltViewModel(),
){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember {
        MapView(context)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver{_,event ->
            when(event){
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    AndroidView(
        factory = {mapView},
        modifier = Modifier.fillMaxSize()
    )

    val markers = remember {
        mutableMapOf<String, Marker>()
    }
    LaunchedEffect(mapView) {
      mapView.getMapAsync {mapLibreMap ->
          mapLibreMap.setStyle(
              Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")
          ){style ->
             friendsLocation.forEach {friend->
                 val existing = markers[friend.friend]

                 if (existing == null && friend.friend!=null) {
                     markers[friend.friend] = mapLibreMap.addMarker(
                         MarkerOptions()
                             .position(LatLng(friend.lat, friend.long))
                             .title(friend.friend)
                     )
                 } else {
                     existing?.position = LatLng(friend.lat, friend.long)
                 }
             }
          }
      }
    }
}

@Preview(showBackground = true)
@Composable
fun mapPreview(){

}