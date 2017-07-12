package br.com.ecostage.mobilecollect

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapActivity : FragmentActivity(), OnMapReadyCallback {

    private val MAP_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (!canAccessLocation()) requestPermissions()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        setupUiSettings(map)

        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        if (canAccessLocation()) {
            map.isMyLocationEnabled = true
        } else {
            requestPermissions()
        }
    }

    private fun setupUiSettings(map: GoogleMap) {
        val uiSettings = map.uiSettings

        uiSettings.isScrollGesturesEnabled = false
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isZoomGesturesEnabled = true
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                MAP_PERMISSION_REQUEST_CODE)
    }

    fun canAccessLocation(): Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}