package br.com.ecostage.mobilecollect.map

class MapPresenterImpl(val mapView: MapView) : MapPresenter {
    override fun showCollect(collectId: Int?, latitude: Double?, longitude: Double?) {
        if (collectId != null) {
            mapView.navigateToCollectActivity(collectId)
        } else if (latitude != null && longitude != null) {
            mapView.navigateToCollectActivity(latitude, longitude)
        }
    }

    override fun mark(latitude: Double, longitude: Double) {
        mapView.showMarkerAt(latitude, longitude)
    }

}