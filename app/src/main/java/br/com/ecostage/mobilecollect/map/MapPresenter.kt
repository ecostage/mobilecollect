package br.com.ecostage.mobilecollect.map

interface MapPresenter {
    fun mark(latitude: Double, longitude: Double)
    fun showCollect(collectId: Int?, latitude: Double?, longitude: Double?)
}