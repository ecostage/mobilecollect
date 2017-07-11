package br.com.ecostage.mobilecollect

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.tasks.Task
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ApiException
import android.support.annotation.NonNull
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.maps.CameraUpdateFactory


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    val LOCATION_REQUEST_CODE = 0
    lateinit var map : GoogleMap
    lateinit var mLocationRequest : LocationRequest
    lateinit var mLocationCallback : LocationCallback
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private val REQUEST_CHECK_SETTINGS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        createLocationRequest()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object:LocationCallback() {
             override fun onLocationResult(locationResult : LocationResult) {
                for (location in locationResult.getLocations()) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.getLatitude(), location.getLongitude()), 14.0f))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (canAccessLocation()) {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        if (canAccessLocation()) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }
            }
        }
    }

    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(5000)
        mLocationRequest.setFastestInterval(5000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnFailureListener(this) { e ->
            val statusCode = (e as ApiException).statusCode
            when (statusCode) {
                CommonStatusCodes.RESOLUTION_REQUIRED ->
                    try {
                        val resolvable = e as ResolvableApiException
                        resolvable.startResolutionForResult(this@MapActivity,
                                REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {

                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    protected fun canAccessLocation() : Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}