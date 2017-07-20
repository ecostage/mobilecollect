package br.com.ecostage.mobilecollect.map

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class MapPresenterImpl(val mapView: MapView) : MapPresenter {

    override fun onPermissionDenied(message: String) = mapView.showMessageAsLongToast(message)


    override fun mark(latitude: Double, longitude: Double) {
        mapView.showMarkerAt(latitude, longitude)
        mapView.takeMapSnapshot()
    }

    override fun takeMapSnapshot() = mapView.takeMapSnapshot()

    override fun onPermissionNeeded() = mapView.showMapPermissionRequestDialog()

    override fun collect(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray) =
        mapView.navigateToCollectActivity(latitude, longitude, compressedMapSnapshot)

    override fun compressMapSnapshot(mapSnapshot: Bitmap?) : ByteArray {
        val stream = ByteArrayOutputStream()
        mapSnapshot?.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val bytes = stream.toByteArray()

        return bytes
    }
}