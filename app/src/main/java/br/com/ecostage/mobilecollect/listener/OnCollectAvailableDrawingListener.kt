package br.com.ecostage.mobilecollect.listener

import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * Listener for collect available drawer component.
 *
 * Created by andremaia on 8/18/17.
 */
interface OnCollectAvailableDrawingListener {
    fun polygonCreated(centralRectangle: PolygonOptions)
    fun geoJsonSourceCreated(source: GeoJsonSource)
    fun layerCreated(layer: Layer)
}