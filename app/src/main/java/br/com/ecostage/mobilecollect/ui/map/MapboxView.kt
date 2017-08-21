package br.com.ecostage.mobilecollect.ui.map

import br.com.ecostage.mobilecollect.model.CollectAvailable
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * Represents a mapbox view operations.
 *
 * Created by andremaia on 8/18/17.
 */
interface MapboxView {
    fun addPolygonForPointsAvailableToCollect(rectangle: PolygonOptions)
    fun addGeoJsonSourceCreated(source: GeoJsonSource)
    fun addLayer(layer: Layer)
    fun addCollectAvailable(collectAvailable: CollectAvailable)
}