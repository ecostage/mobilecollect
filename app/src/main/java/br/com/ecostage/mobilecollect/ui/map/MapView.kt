package br.com.ecostage.mobilecollect.ui.map

import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel

interface MapView {
    fun showMapPermissionRequestDialog()
    fun removeLastMarkerFromMap()
    fun showMarkerAt(latitude: Double, longitude: Double) : Int
    fun navigateToCollectActivity(collectId: Int)
    fun navigateToCollectActivity(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray)
    fun takeMapSnapshot()
    fun showMessageAsLongToast(message: String)
    fun populateMarker(markerIndex: Int, collectViewModel: CollectViewModel?, showInfo: Boolean)
    fun centralizeMapCameraAt(latitude: Double, longitude: Double)
}