package br.com.ecostage.mobilecollect.map

interface MapView {
    fun showMapPermissionRequestDialog()
    fun removeLastMarkerFromMap()
    fun showMarkerAt(latitude: Double, longitude: Double)
    fun navigateToCollectActivity(collectId: Int)
    fun navigateToCollectActivity(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray)
    fun takeMapSnapshot()
    fun showMessageAsLongToast(message: String)
}