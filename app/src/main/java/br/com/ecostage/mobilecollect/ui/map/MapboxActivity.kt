package br.com.ecostage.mobilecollect.ui.map

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.collect.CollectActivity
import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.offline.*
import kotlinx.android.synthetic.main.activity_mapbox.*
import org.jetbrains.anko.*
import org.json.JSONObject


class MapboxActivity : AppCompatActivity(),
        br.com.ecostage.mobilecollect.ui.map.MapView,
        MapboxMap.SnapshotReadyCallback,
        MapboxMap.OnMarkerClickListener,
        AnkoLogger {

    companion object {
        val COLLECT_REQUEST = 1
        val COLLECT_DATA_RESULT = "MapboxActivity:collectDataResult"
        val MAP_PERMISSION_REQUEST_CODE = 1
    }

    private val mapPresenter: MapPresenter = MapPresenterImpl(this)
    private var mapBox: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_auth_token))
        setContentView(R.layout.activity_mapbox)
        mapView?.onCreate(savedInstanceState)

        setupMap()
        setupView()

        mapPresenter.loadUserCollects()
    }

    private fun setupView() {
        supportActionBar?.title = resources.getString(R.string.map)

        // Test only purpose
//        downloadTilesButton.setOnClickListener{ downloadTile() }

        startCollectButton.setOnClickListener {

            accessingLocationInfo {
                val latitude = mapBox?.myLocation?.latitude
                val longitude = mapBox?.myLocation?.longitude
                if (latitude != null && longitude != null)
                    mapPresenter.mark(latitude, longitude)
            }
        }

        myLocationButton.setOnClickListener { moveCameraToMyLocation() }
    }

    private fun moveCameraToMyLocation() {
        val latitude = mapBox?.myLocation?.latitude
        val longitude = mapBox?.myLocation?.longitude

        if (latitude != null && longitude != null) {
            centralizeMapCameraAt(latitude, longitude)
        }
    }

    private fun setupMap() {
        mapView?.getMapAsync {
            mapBox = it
            mapBox?.isMyLocationEnabled = true

            moveCameraToMyLocation()
        }
    }

    fun downloadTile() {
        val offlineManager = OfflineManager.getInstance(this)
        val bounds = LatLngBounds.Builder()
                .include(LatLng(-23.417762, -46.415311)) // Northeast
                .include(LatLng(-23.669870, -46.770609)) // Southwest
                .build()

        val definition = OfflineTilePyramidRegionDefinition(mapBox?.styleUrl,
                bounds,
                10.0,
                20.0,
                this.resources.displayMetrics.density)

        val metadata: ByteArray
        val jsonObject = JSONObject()
        jsonObject.put("region_name", "São Paulo")
        val json = jsonObject.toString()
        metadata = json.toByteArray()

        offlineManager.createOfflineRegion(definition, metadata, object : OfflineManager.CreateOfflineRegionCallback {
            override fun onCreate(offlineRegion: OfflineRegion?) {

                offlineRegion?.setDownloadState(OfflineRegion.STATE_ACTIVE)

                // Monitor the download progress using setObserver
                offlineRegion?.setObserver(object : OfflineRegion.OfflineRegionObserver {
                    override fun onStatusChanged(status: OfflineRegionStatus) {

                        // Calculate the download percentage
                        val percentage = if (status.requiredResourceCount >= 0)
                            100.0 * status.completedResourceCount / status.requiredResourceCount
                        else
                            0.0

                        if (status.isComplete) {
                            // Download complete
                            info("Region download successfully")
                        } else if (status.isRequiredResourceCountPrecise) {
                            info(percentage)
                        }
                    }

                    override fun onError(error: OfflineRegionError) {
                        // If an error occurs, print to logcat
                        error("onError reason: " + error.reason)
                        error("onError message: " + error.message)
                    }

                    override fun mapboxTileCountLimitExceeded(limit: Long) {
                        // Notify if offline region exceeds maximum tile count
                        error("Mapbox tile count limit exceeded: " + limit)
                    }
                })
            }

            override fun onError(error: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

//    override fun getContentViewId(): Int {
//        return R.layout.activity_mapbox
//    }
//
//    override fun getNavigationMenuItemId(): Int {
//        return R.id.action_map
//    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) mapView?.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            COLLECT_REQUEST -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    mapPresenter.removeLastMarker()
                } else if (resultCode == Activity.RESULT_OK) {
                    // Populate marker
                    val collectViewModel: CollectViewModel? = data?.getParcelableExtra<CollectViewModel>(COLLECT_DATA_RESULT)
                    val markerLastIndex = mapBox?.markers?.lastIndex
                    if (markerLastIndex != null) {
                        this.populateMarker(markerLastIndex, collectViewModel, showInfo = true)
                        val marker = mapBox?.markers?.last()
                        if (marker != null)
                            this.centralizeMapCameraAt(marker.position.latitude, marker.position.longitude)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MAP_PERMISSION_REQUEST_CODE -> if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap()
            } else {
                mapPresenter.onPermissionDenied(resources.getString(R.string.map_permission_needed))
            }
        }
    }

    override fun onSnapshotReady(snapshot: Bitmap?) {
        val marker = mapBox?.markers?.last()
        if (marker != null && snapshot != null) {
            val compressedSnapshot = mapPresenter.compressMapSnapshot(snapshot)
            val lastMarkerPosition = marker.position

            mapPresenter.collect(lastMarkerPosition.latitude, lastMarkerPosition.longitude, compressedSnapshot)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker is MapboxCustomMarker) {
            val options = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.abc_fade_in, R.anim.abc_fade_out)
            startActivity(intentFor<CollectActivity>(CollectActivity.COLLECT_ID to marker.tag),
                    options.toBundle())
            return true
        }

        return false
    }

    override fun showMapPermissionRequestDialog() {

        Snackbar.make(mapBoxContainerView, R.string.message_snackbar_location_not_allowed, Snackbar.LENGTH_SHORT)
                .show()

        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                MAP_PERMISSION_REQUEST_CODE)
    }

    override fun removeLastMarkerFromMap() {
        val lastMarker = mapBox?.markers?.last()
        if (lastMarker != null)
            mapBox?.removeMarker(lastMarker)

    }

    override fun showMarkerAt(latitude: Double, longitude: Double): Int {
        val position: LatLng = LatLng(latitude, longitude)
        val iconFactory = IconFactory.getInstance(this)

        mapBox?.addMarker(MarkerOptions()
                .position(position)
                .icon(iconFactory.fromBitmap(createPinMap()!!)))

        val lastIndex = mapBox?.markers?.lastIndex

        return lastIndex ?: 0
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

    override fun takeMapSnapshot() {
        mapBox?.snapshot(this)
    }

    override fun showMessageAsLongToast(message: String) {
        longToast(message)
    }

    override fun populateMarker(markerIndex: Int, collectViewModel: CollectViewModel?, showInfo: Boolean) {
        val marker = mapBox?.markers?.get(markerIndex)
        if (marker != null) {
//            marker.tag = collectViewModel?.id
            marker.title = collectViewModel?.name
            marker.snippet = "Classificacão: " + collectViewModel?.classification + " \n Data: " + collectViewModel?.date
//
//            if (showInfo)
//                marker.showInfoWindow(mapBox, mapView)
        }
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

    override fun centralizeMapCameraAt(latitude: Double, longitude: Double) {
        mapBox?.easeCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
    }

    // TODO Move this to another place to avoid copy and paste
    private fun canAccessLocation(): Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    // TODO Move this to another place to avoid copy and paste
    fun accessingLocationInfo(body: () -> Unit) {
        if (canAccessLocation()) {
            body()
        } else {
            mapPresenter.onPermissionNeeded()
        }
    }

}
