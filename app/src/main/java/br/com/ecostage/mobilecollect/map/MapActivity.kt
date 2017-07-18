package br.com.ecostage.mobilecollect.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import br.com.ecostage.mobilecollect.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapActivity : AppCompatActivity(),
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        MapView {

    private val mapPresenter: MapPresenter = MapPresenterImpl(this)
    private val MAP_PERMISSION_REQUEST_CODE = 1
    private var googleApiClient: GoogleApiClient? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setupView()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        setupUiSettings(googleMap)

        if (canAccessLocation()) {
            buildGoogleApiClient()
            map.isMyLocationEnabled = true
        } else {
            showMapPermissionRequest()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(bundle: Bundle?) {
        Log.i(MapActivity::class.java.simpleName, "Location services connected")

        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (canAccessLocation()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)

            val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
            if (lastLocation != null) {
                val zoomLevel = 16f
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude, lastLocation.longitude), zoomLevel))
            }
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(MapActivity::class.java.simpleName, "Location services suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(MapActivity::class.java.simpleName, "Location services failed to connect")
    }

    override fun onPause() {
        super.onPause()

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
    }

    override fun onLocationChanged(location: Location?) {}

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            MAP_PERMISSION_REQUEST_CODE -> if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleApiClient == null) {
                    buildGoogleApiClient()
                }

                googleMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, resources.getString(R.string.map_permission_needed), Toast.LENGTH_LONG).show()
            }
        }
    }

    @Synchronized private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient?.connect()
    }

    private fun setupUiSettings(map: GoogleMap) {
        val uiSettings = map.uiSettings

        uiSettings.isZoomControlsEnabled = true
    }

    override fun showMapPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                MAP_PERMISSION_REQUEST_CODE)
    }

    private fun setupView() {
        supportActionBar?.title = resources.getString(R.string.map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun canAccessLocation(): Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}