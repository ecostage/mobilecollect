package br.com.ecostage.mobilecollect.map

class MapPresenterImpl(val mapView: MapView) : MapPresenter {
    override fun mark(latitude: Double?, longitude: Double?) {
        mapView.showMarkerAt(latitude, longitude)
    }

}