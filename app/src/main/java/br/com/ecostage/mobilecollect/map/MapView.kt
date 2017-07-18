package br.com.ecostage.mobilecollect.map

interface MapView {
    fun showMapPermissionRequest()
    fun removeMarkers()
    fun showMarkerAt(latitude: Double, longitude: Double)
    fun navigateToCollectActivity(collectId: Int)
    fun navigateToCollectActivity(latitude: Double, longitude: Double)
}