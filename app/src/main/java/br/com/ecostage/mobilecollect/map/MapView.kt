package br.com.ecostage.mobilecollect.map

import br.com.ecostage.mobilecollect.collect.CollectViewModel
import com.google.android.gms.maps.model.Marker

interface MapView {
    fun showMapPermissionRequestDialog()
    fun removeLastMarkerFromMap()
    fun showMarkerAt(latitude: Double, longitude: Double) : Marker
    fun navigateToCollectActivity(collectId: Int)
    fun navigateToCollectActivity(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray)
    fun takeMapSnapshot()
    fun showMessageAsLongToast(message: String)
    fun populateMarker(marker: Marker, collectViewModel: CollectViewModel?, showInfo: Boolean)
}