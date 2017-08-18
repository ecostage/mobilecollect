package br.com.ecostage.mobilecollect.ui.map

import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions

/**
 * Created by cmaia on 8/18/17.
 */
class MapboxCustomMarker(val markerOptions: MarkerOptions, val tag: String) : Marker(markerOptions) {

}