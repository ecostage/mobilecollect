package br.com.ecostage.mobilecollect.map

interface MapView {
    fun showMapPermissionRequest()
    fun removeMarkers()
    fun showMarkerAt(latitude: Double?, longitude: Double?)
}