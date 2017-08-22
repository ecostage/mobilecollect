package br.com.ecostage.mobilecollect.ui.map

import com.mapbox.mapboxsdk.annotations.Marker

/**
 * Created by cmaia on 8/18/17.
 */
data class MapboxCustomMarker(val markerOptionsMapbox: MapboxCustomMarkerOptions, var tag: String) : Marker(markerOptionsMapbox)