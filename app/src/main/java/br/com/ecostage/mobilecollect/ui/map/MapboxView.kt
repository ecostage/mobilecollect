package br.com.ecostage.mobilecollect.ui.map

import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * Created by andremaia on 8/18/17.
 */
interface MapboxView {
    fun addPolygonForPointsAvailableToCollect(rectangle: PolygonOptions)
    fun addGeoJsonSourceCreated(source: GeoJsonSource)
    fun addLineLayer(layer: LineLayer)
}