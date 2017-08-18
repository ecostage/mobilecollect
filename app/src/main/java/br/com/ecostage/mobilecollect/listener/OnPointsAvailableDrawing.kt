package br.com.ecostage.mobilecollect.listener

import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * Created by andremaia on 8/18/17.
 */
interface OnPointsAvailableDrawing {
    fun polygonCreated(centralRectangle: PolygonOptions)
    fun geoJsonSourceCreated(source: GeoJsonSource)
    fun lineLayerCreated(lineLayer: LineLayer)
}