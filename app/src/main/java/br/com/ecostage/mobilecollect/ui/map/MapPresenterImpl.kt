package br.com.ecostage.mobilecollect.ui.map

import android.graphics.Bitmap
import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.mapper.CollectMapper
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.util.ImageUtil

class MapPresenterImpl(val mapView: MapView,
                       val activity: MapActivity)
    : MapPresenter, OnCollectLoadedListener {

    private val mapInteractor : MapInteractor = MapInteractorImpl(this, activity)

    override fun onPermissionDenied(message: String) = mapView.showMessageAsLongToast(message)

    override fun mark(latitude: Double, longitude: Double) {
        mapView.centralizeMapCameraAt(latitude, longitude)
        mapView.showMarkerAt(latitude, longitude)
        mapView.takeMapSnapshot()
    }

    override fun takeMapSnapshot() = mapView.takeMapSnapshot()

    override fun onPermissionNeeded() = mapView.showMapPermissionRequestDialog()

    override fun collect(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray) =
        mapView.navigateToCollectActivity(latitude, longitude, compressedMapSnapshot)

    override fun compressMapSnapshot(mapSnapshot: Bitmap) : ByteArray {
        return ImageUtil.compress(mapSnapshot, Bitmap.CompressFormat.JPEG,30)
    }

    override fun removeLastMarker() = mapView.removeLastMarkerFromMap()

    override fun loadUserCollects() {
        mapInteractor.loadUserCollects()
    }

    override fun onCollectLoaded(collect: Collect) {
        // This was used to avoid smart cast impossible problem
        collect.latitude?.let { latitude ->
            collect.longitude?.let { longitude ->
                val marker = mapView.showMarkerAt(latitude, longitude)
                val collectViewModel = CollectMapper().map(collect)
                mapView.populateMarker(marker, collectViewModel, false)
            }
        }
    }

    override fun onCollectLoadedError() {
        // no-op
    }

    override fun onCollectImageLoaded(collect: Collect) {
        // no-op
    }

    override fun onCollectImageLoadedError() {
        // no-op
    }
}