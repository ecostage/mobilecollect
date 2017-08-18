package br.com.ecostage.mobilecollect.ui.map

import android.graphics.Bitmap
import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.listener.OnPointsAvailableDrawing
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel
import br.com.ecostage.mobilecollect.ui.helper.PointsAvailableToCollectDrawer
import br.com.ecostage.mobilecollect.util.ImageUtil
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapPresenterImpl(val mapView: MapView, val mapboxView: MapboxView? = null) :
        MapPresenter,
        OnCollectLoadedListener,
        OnPointsAvailableDrawing {

    private val mapInteractor: MapInteractor = MapInteractorImpl(this)
    private val pointsAvailableToCollectDrawer = PointsAvailableToCollectDrawer(this)

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

    override fun compressMapSnapshot(mapSnapshot: Bitmap): ByteArray {
        return ImageUtil.compress(mapSnapshot, Bitmap.CompressFormat.JPEG, 30)
    }

    override fun removeLastMarker() = mapView.removeLastMarkerFromMap()

    override fun loadUserCollects() {
        mapInteractor.loadUserCollects()
    }

    override fun onCollectLoaded(collect: Collect) {
        // This was used to avoid smart cast impossible problem
        collect.latitude?.let { latitude ->
            collect.longitude?.let { longitude ->
                val markerIndex = mapView.showMarkerAt(latitude, longitude)

                val collectViewModel = CollectViewModel()

                collectViewModel.id = collect.id
                collectViewModel.name = collect.name
                collectViewModel.latitude = collect.latitude
                collectViewModel.longitude = collect.longitude
                collectViewModel.classification = collect.classification
                collectViewModel.userId = collect.userId
                collectViewModel.date = collect.date
                collectViewModel.photo = collect.photo

                mapView.populateMarker(markerIndex, collectViewModel, false)
            }
        }
    }

    override fun onCollectLoadedError() {
        // no-op
    }


    override fun drawRectangles(position: LatLng) {
        pointsAvailableToCollectDrawer.drawRectangles(position)
    }

    override fun polygonCreated(centralRectangle: PolygonOptions) {
        mapboxView?.addPolygonForPointsAvailableToCollect(centralRectangle)
    }

    override fun geoJsonSourceCreated(source: GeoJsonSource) {
        mapboxView?.addGeoJsonSourceCreated(source)
    }

    override fun lineLayerCreated(lineLayer: LineLayer) {
        mapboxView?.addLineLayer(lineLayer)
    }
}