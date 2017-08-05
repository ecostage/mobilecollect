package br.com.ecostage.mobilecollect.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.collect.CollectActivity
import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast

@SuppressLint("MissingPermission")
class MapActivity : BottomNavigationActivity(),
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.SnapshotReadyCallback,
        GoogleMap.OnMarkerClickListener,
        LocationListener,
        AnkoLogger,
        MapView {

    companion object {
        val COLLECT_REQUEST = 1
        val COLLECT_DATA_RESULT = "MapActivity:collectDataResult"
    }

    private val mapPresenter: MapPresenter = MapPresenterImpl(this, this)
    private val MAP_PERMISSION_REQUEST_CODE = 1
    private var googleApiClient: GoogleApiClient? = null
    private val markers: MutableList<Marker> = ArrayList()

    private lateinit var googleMap: GoogleMap
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupMap()

        // Load collects to show in map
        mapPresenter.loadUserCollects()
    }


    private fun setupView() {
        supportActionBar?.title = resources.getString(R.string.map)

        addNewCollectFloatingActionButton.setOnClickListener {

            accessingLocationInfo {
                val currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                mapPresenter.mark(currentLocation.latitude, currentLocation.longitude)
            }
        }

    }

    fun accessingLocationInfo(body: () -> Unit) {
        if (canAccessLocation()) {
            body()
        } else {
            mapPresenter.onPermissionNeeded()
        }
    }


    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            COLLECT_REQUEST -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    mapPresenter.removeLastMarker()
                } else if (resultCode == Activity.RESULT_OK) {
                    // Populate marker
                    val collectViewModel: CollectViewModel? = data?.getParcelableExtra<CollectViewModel>(COLLECT_DATA_RESULT)
                    val marker = markers.last()
                    this.populateMarker(marker, collectViewModel, showInfo = true)
                    this.centralizeMapCameraAt(marker.position.latitude, marker.position.longitude)
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        googleMap.setOnMarkerClickListener(this)

        accessingLocationInfo {
            buildGoogleApiClient()
            map.isMyLocationEnabled = true
        }
    }


    override fun onConnected(bundle: Bundle?) {
        info("Location services connected")

        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        accessingLocationInfo {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)

            val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
            if (lastLocation != null) {
                val zoomLevel = 16f
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude, lastLocation.longitude), zoomLevel))
            }
        }
    }

    override fun onConnectionSuspended(i: Int) {
        info(R.string.map_info_connection_suspended)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        error(R.string.map_error_connection_failed)
    }

    override fun onPause() {
        super.onPause()

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
    }

    override fun onLocationChanged(location: Location?) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MAP_PERMISSION_REQUEST_CODE -> if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleApiClient == null) {
                    buildGoogleApiClient()
                }

                googleMap.isMyLocationEnabled = true
            } else {
                mapPresenter.onPermissionDenied(resources.getString(R.string.map_permission_needed))
            }
        }
    }

    override fun onSnapshotReady(mapSnapshot: Bitmap?) {
        if (!markers.isEmpty() && mapSnapshot != null) {
            val compressedSnapshot = mapPresenter.compressMapSnapshot(mapSnapshot)
            val lastMarkerPosition = markers.last().position

            mapPresenter.collect(lastMarkerPosition.latitude, lastMarkerPosition.longitude, compressedSnapshot)
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.tag != null) {
            val options = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.abc_fade_in, R.anim.abc_fade_out)
            startActivity(intentFor<CollectActivity>(CollectActivity.COLLECT_ID to marker.tag as String),
                    options.toBundle())
            return true
        }

        return false
    }

    @Synchronized private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient?.connect()
    }

    override fun showMapPermissionRequestDialog() {

        Snackbar.make(mapContainerView, R.string.message_snackbar_location_not_allowed, Snackbar.LENGTH_SHORT)
                .show()

        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                MAP_PERMISSION_REQUEST_CODE)
    }

    private fun canAccessLocation(): Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun showMarkerAt(latitude: Double, longitude: Double): Marker {
        val position: LatLng = LatLng(latitude, longitude)
        val descriptor = BitmapDescriptorFactory.fromBitmap(createPinMap())
        val marker = googleMap.addMarker(MarkerOptions()
                .position(position)
                .icon(descriptor))

        markers.add(marker)

        return marker
    }

    private fun createPinMap(): Bitmap? {
        val px = 42
        val bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val shape = resources.getDrawable(R.drawable.shape_pin_collect, theme) as GradientDrawable

        val color = getColor(R.color.default_pin)
        shape.setStroke(3, color)
        shape.setColor(color)

        shape.setBounds(0, 0, bitmap.width, bitmap.height)
        shape.draw(canvas)
        return bitmap
    }

    override fun takeMapSnapshot() {
        googleMap.snapshot(this)
    }

    override fun navigateToCollectActivity(collectId: Int) {
        val options = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.fade_in, R.anim.fade_out)
        startActivity(intentFor<CollectActivity>(CollectActivity.COLLECT_ID to collectId.toString()), options.toBundle())
    }

    override fun navigateToCollectActivity(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray) {
        val options = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.fade_in, R.anim.fade_out)
        startActivityForResult(intentFor<CollectActivity>(CollectActivity.MARKER_LATITUDE to latitude.toString(),
                CollectActivity.MARKER_LONGITUDE to longitude.toString(),
                CollectActivity.COMPRESSED_MAP_SNAPSHOT to compressedMapSnapshot),
                COLLECT_REQUEST,
                options.toBundle())
    }

    override fun removeLastMarkerFromMap() {
        if (!markers.isEmpty()) {
            markers.last().remove()
        }
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_map
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_map
    }

    override fun showMessageAsLongToast(message: String) {
        longToast(message)
    }

    override fun populateMarker(marker: Marker, collectViewModel: CollectViewModel?, showInfo: Boolean) {
        marker.tag = collectViewModel?.id
        marker.title = collectViewModel?.name
        marker.snippet = "Classificacão: " + collectViewModel?.classification + " \n Data: " + collectViewModel?.date

        if (showInfo)
            marker.showInfoWindow()
    }

    override fun centralizeMapCameraAt(latitude: Double, longitude: Double) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
    }
}