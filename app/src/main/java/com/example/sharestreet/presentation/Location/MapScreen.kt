package com.example.sharestreet.presentation.Location

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.sharestreet.utils.LocationTrackingService
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import kotlin.collections.get

@Composable
fun MapScreen(
    friendsLocation : List<FriendLocationModel>,
    locationViewModel : LocationViewModel = hiltViewModel(),
){

    val context = LocalContext.current
    var permissionRefresh by remember { mutableStateOf(0) }
    val notificationPermission = remember(permissionRefresh) {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    val locationPermission = remember(permissionRefresh) {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )== PackageManager.PERMISSION_GRANTED
    }
    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {permissions->
        permissions.forEach {(permission,granted)->
           permissionRefresh++
        }
    }
    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {permission->
       permissionRefresh++
    }
    LaunchedEffect(locationPermission,notificationPermission) {
        if(!locationPermission){
            locationLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
        else if(!notificationPermission){
            notificationLauncher.launch(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }
    LaunchedEffect(locationPermission, notificationPermission) {
        if (locationPermission && notificationPermission) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = LocationTrackingService.ACTION_START
            }
            ContextCompat.startForegroundService(context, intent)
        }
    }
    if(locationPermission && notificationPermission) {
        MAP(friendsLocation)
    }
    else{
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column (
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Permissions Required",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (!locationPermission)
                            "ShareStreet needs location permission to display your location and your friends on the map."
                        else
                            "ShareStreet needs notification permission so you can receive friend requests and location updates.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (!locationPermission) {
                                locationLauncher.launch(
                                    arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            } else {
                                notificationLauncher.launch(
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                )
                            }
                        }
                    ) {
                        Text(
                            if (!locationPermission)
                                "Grant Location Permission"
                            else
                                "Grant Notification Permission"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView{
    val context = LocalContext.current
    val mapView = remember {
        MapLibre.getInstance(context)
        MapView(context).apply { onCreate(null) }
    }
    val lifecycleObserevr = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserevr)
        onDispose {
            lifecycle.removeObserver(lifecycleObserevr)
        }
    }
    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver{_,event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
    }

private const val SOURCE_ID = "markers-source"
private const val LAYER_ID = "markers-layer"
private const val ICON_ID = "marker-icon"
@Composable
fun MAP(
    friendsLocation: List<FriendLocationModel>
){
    val mapView = rememberMapViewWithLifecycle()
    var style by remember { mutableStateOf<Style?>(null) }

    AndroidView(
        factory = {mapView},
    )

    LaunchedEffect(Unit) {
        mapView.getMapAsync { map->
            map.setStyle("https://demotiles.maplibre.org/style.json"){loadedStyle->
                val iconBitmap = generateMarkerBitmap()
                loadedStyle.addImage(ICON_ID,iconBitmap)
                loadedStyle.addSource(GeoJsonSource(SOURCE_ID))
                loadedStyle.addLayer(
                    SymbolLayer(LAYER_ID,SOURCE_ID).withProperties(
                        PropertyFactory.iconImage(ICON_ID),
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconSize(1.0f),

                        PropertyFactory.textField(Expression.get("name")),
                        PropertyFactory.textSize(14f),
                        PropertyFactory.textColor(Color.BLACK),
                        PropertyFactory.textHaloColor(Color.WHITE),
                        PropertyFactory.textHaloWidth(1.5f),
                        PropertyFactory.textOffset(arrayOf(0f, 1.5f)),   // push text below the icon
                        PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
                        PropertyFactory.textAllowOverlap(true),
                        PropertyFactory.textIgnorePlacement(true)
                    )
                )
                style = loadedStyle
            }
        }
    }
    LaunchedEffect(friendsLocation,style) {
        val currentStyle = style?:return@LaunchedEffect
        val features = friendsLocation.map {
            Feature.fromGeometry(Point.fromLngLat(it.long,it.lat)).apply {
                addStringProperty("id",it.friend)
                addStringProperty("name",it.friend)
            }
        }
        currentStyle.getSourceAs<GeoJsonSource>(SOURCE_ID)
            ?.setGeoJson(FeatureCollection.fromFeatures(features))
    }
}
fun generateMarkerBitmap(colorHex: String = "#FF5722"): Bitmap {
    val size = 60
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(colorHex)
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2.5f, paint)
    paint.apply { color = Color.WHITE; style = Paint.Style.STROKE; strokeWidth = 4f }
    canvas.drawCircle(size / 2f, size / 2f, size / 2.5f, paint)
    return bitmap
}
@Preview(showBackground = true)
@Composable
fun mapPreview(){

}