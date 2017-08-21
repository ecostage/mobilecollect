package br.com.ecostage.mobilecollect.ui.helper

import android.graphics.Color
import br.com.ecostage.mobilecollect.listener.OnPointsAvailableDrawing
import br.com.ecostage.mobilecollect.model.CollectAvailable
import com.google.maps.android.SphericalUtil
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.commons.geojson.Feature
import com.mapbox.services.commons.geojson.FeatureCollection
import com.mapbox.services.commons.geojson.LineString
import com.mapbox.services.commons.models.Position
import java.util.*

/**
 * Created by andremaia on 8/18/17.
 */
class CollectAvailableDrawer(val onPointsAvailableDrawing: OnPointsAvailableDrawing) {

    // Should be the half of the diameter -> Diameter = Radius * 2
    private val radiusInMeters = 15.0

    fun drawRectangles(collectAvailable: CollectAvailable) {
        val position = LatLng(collectAvailable.latitude!!, collectAvailable.longitude!!)
        drawRectangles(position)
    }

    fun drawRectangles(position: LatLng) {
        val localSquare = toBounds(position, radiusInMeters)

        val northwest = LatLng(localSquare.southWest.latitude, localSquare.northEast.longitude)
        val southeast = LatLng(localSquare.northEast.latitude, localSquare.southWest.longitude)

        val lineOne = position.latitude - ((northwest.latitude - position.latitude) * 2)
        val lineTwo = position.latitude
        val lineThree = position.latitude - ((southeast.latitude - position.latitude) * 2)

        val columnOne = position.longitude - ((northwest.longitude - position.longitude) * 2)
        val columnTwo = position.longitude
        val columnThree = position.longitude - ((southeast.longitude - position.longitude) * 2)

        val r1 = LatLng(lineOne, columnOne)
        val r2 = LatLng(lineOne, columnTwo)
        val r3 = LatLng(lineOne, columnThree)
        val r4 = LatLng(lineTwo, columnOne)
        val r5 = LatLng(lineTwo, columnTwo)
        val r6 = LatLng(lineTwo, columnThree)
        val r7 = LatLng(lineThree, columnOne)
        val r8 = LatLng(lineThree, columnTwo)
        val r9 = LatLng(lineThree, columnThree)

        drawRectangle(r1, Color.CYAN)
        drawRectangle(r2, Color.WHITE)
        drawRectangle(r3, Color.MAGENTA)
        drawRectangle(r4, Color.BLUE)
        drawRectangle(r5, Color.GREEN)
        drawRectangle(r6, Color.RED)
        drawRectangle(r7, Color.YELLOW)
        drawRectangle(r8, Color.BLACK)
        drawRectangle(r9, Color.GRAY)
    }


    fun drawRectangle(position: LatLng, color: Int) {
        val radiusInMeters = 15.0
        val localSquare = toBounds(position, radiusInMeters)

        val northwest = LatLng(localSquare.southWest.latitude, localSquare.northEast.longitude)
        val southeast = LatLng(localSquare.northEast.latitude, localSquare.southWest.longitude)

        drawSolidPolygons(color, localSquare, northwest, southeast)
        drawLineLayer(localSquare, northwest, southeast, color)
    }

    private fun drawLineLayer(localSquare: LatLngBounds, northwest: LatLng, southeast: LatLng, color: Int) {

        val coordinates = rectangleCoordinates(localSquare, northwest, southeast)

        val lineString = LineString.fromCoordinates(coordinates)
        val featureCollection = FeatureCollection.fromFeatures(arrayOf(Feature.fromGeometry(lineString)))
        val sourceId = "line-source" + UUID.randomUUID()

        // Event
        val geoJsonSource = GeoJsonSource(sourceId, featureCollection)
        onPointsAvailableDrawing.geoJsonSourceCreated(geoJsonSource)

        val lineLayerId = "linelayer" + UUID.randomUUID()
        val lineLayer = LineLayer(lineLayerId, sourceId)
        lineLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(color)
        )

        // Event
        onPointsAvailableDrawing.lineLayerCreated(lineLayer)
    }

    private fun rectangleCoordinates(localSquare: LatLngBounds, northwest: LatLng, southeast: LatLng): List<Position> {
        val coordinates = listOf(
                Position.fromLngLat(localSquare.northEast.longitude, localSquare.northEast.latitude),
                Position.fromLngLat(northwest.longitude, northwest.latitude),
                Position.fromLngLat(localSquare.southWest.longitude, localSquare.southWest.latitude),
                Position.fromLngLat(southeast.longitude, southeast.latitude),
                Position.fromLngLat(localSquare.northEast.longitude, localSquare.northEast.latitude)
        )
        return coordinates
    }

    private fun drawSolidPolygons(color: Int, localSquare: LatLngBounds, northwest: LatLng, southeast: LatLng) {
        val centralRectangle = PolygonOptions()
                .strokeColor(color)
                .fillColor(Color.TRANSPARENT)

        centralRectangle.add(localSquare.northEast, northwest)
                .add(northwest, localSquare.southWest)
                .add(localSquare.southWest, southeast)
                .add(southeast, localSquare.northEast)


        // Event
        if (centralRectangle != null) {
            onPointsAvailableDrawing.polygonCreated(centralRectangle)
        }
    }


    fun toBounds(center: LatLng, radiusInMeters: Double): LatLngBounds {
        val distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0)

        val gCenter = toGoogleLatLng(center)

        val southwestCorner =
                SphericalUtil.computeOffset(gCenter, distanceFromCenterToCorner, 225.0)
        val northeastCorner =
                SphericalUtil.computeOffset(gCenter, distanceFromCenterToCorner, 45.0)


        return LatLngBounds.Builder().include(fromGoogleLatLng(northeastCorner)).include(fromGoogleLatLng(southwestCorner)).build()
    }

    private fun toGoogleLatLng(latLng: LatLng): com.google.android.gms.maps.model.LatLng {
        return com.google.android.gms.maps.model.LatLng(latLng.latitude, latLng.longitude)
    }

    private fun fromGoogleLatLng(latLng: com.google.android.gms.maps.model.LatLng): LatLng {
        return LatLng(latLng.latitude, latLng.longitude)
    }
}