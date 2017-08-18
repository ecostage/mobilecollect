package br.com.ecostage.mobilecollect.ui.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.offline.*
import kotlinx.android.synthetic.main.activity_mapbox.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.json.JSONObject


class MapboxActivity : AppCompatActivity(), br.com.ecostage.mobilecollect.ui.map.MapView, AnkoLogger {

    companion object {
        val COLLECT_REQUEST = 1
        val COLLECT_DATA_RESULT = "MapboxActivity:collectDataResult"
    }

    private val mapPresenter: MapPresenter = MapPresenterImpl(this)
    private var mapView: MapView? = null
    private var mapBox: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_auth_token))
        setContentView(R.layout.activity_mapbox)

        setupMap(savedInstanceState)
        setupView()
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

        if (latitude != null && longitude != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
            mapBox?.easeCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        mapView = findViewById(R.id.mapView) as MapView
        mapView?.onCreate(savedInstanceState)

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

    override fun showMapPermissionRequestDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeLastMarkerFromMap() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMarkerAt(latitude: Double, longitude: Double): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToCollectActivity(collectId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToCollectActivity(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeMapSnapshot() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessageAsLongToast(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun populateMarker(markerIndex: Int, collectViewModel: CollectViewModel?, showInfo: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun centralizeMapCameraAt(latitude: Double, longitude: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
