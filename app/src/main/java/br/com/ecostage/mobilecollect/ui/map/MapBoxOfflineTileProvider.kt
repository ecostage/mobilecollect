package br.com.ecostage.mobilecollect.ui.map


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.google.android.gms.maps.model.TileProvider.NO_TILE
import java.io.Closeable
import java.io.File


/**
 * Created by cmaia on 8/27/17.
 */

class MapBoxOfflineTileProvider : TileProvider, Closeable {

    // ------------------------------------------------------------------------
    // Instance Variables
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // Public Methods
    // ------------------------------------------------------------------------

    /**
     * The minimum zoom level supported by this provider.

     * @return the minimum zoom level supported or [Integer.MIN_VALUE] if
     * *         it could not be determined.
     */
    var minimumZoom = Integer.MIN_VALUE
        private set

    /**
     * The maximum zoom level supported by this provider.

     * @return the maximum zoom level supported or [Integer.MAX_VALUE] if
     * *         it could not be determined.
     */
    var maximumZoom = Integer.MAX_VALUE
        private set

    /**
     * The geographic bounds available from this provider.

     * @return the geographic bounds available or [null] if it could not
     * *         be determined.
     */
    var bounds: LatLngBounds? = null
        private set

    private var mDatabase: SQLiteDatabase? = null

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    constructor(file: File) : this(file.absolutePath)

    constructor(pathToFile: String) {
        val flags = SQLiteDatabase.OPEN_READONLY or SQLiteDatabase.NO_LOCALIZED_COLLATORS
        this.mDatabase = SQLiteDatabase.openDatabase(pathToFile, null, flags)
        this.calculateZoomConstraints()
        this.calculateBounds()
    }

    // ------------------------------------------------------------------------
    // TileProvider Interface
    // ------------------------------------------------------------------------

    override fun getTile(x: Int, y: Int, z: Int): Tile {
        var tile = NO_TILE
        if (this.isZoomLevelAvailable(z) && this.isDatabaseAvailable) {
            val projection = arrayOf("tile_data")
            val row = (Math.pow(2.0, z.toDouble()) - y).toInt() - 1
            val predicate = "tile_row = ? AND tile_column = ? AND zoom_level = ?"
            val values = arrayOf(row.toString(), x.toString(), z.toString())
            val c = this.mDatabase!!.query("tiles", projection, predicate, values, null, null, null)
            if (c != null) {
                c.moveToFirst()
                if (!c.isAfterLast) {
                    tile = Tile(256, 256, c.getBlob(0))
                }
                c.close()
            }
        }
        return tile
    }

    // ------------------------------------------------------------------------
    // Closeable Interface
    // ------------------------------------------------------------------------

    /**
     * Closes the provider, cleaning up any background resources.

     *
     *
     * You must call [.close] when you are finished using an instance of
     * this provider. Failing to do so may leak resources, such as the backing
     * SQLiteDatabase.
     *
     */
    override fun close() {
        if (this.mDatabase != null) {
            this.mDatabase!!.close()
            this.mDatabase = null
        }
    }

    /**
     * Determines if the requested zoom level is supported by this provider.

     * @param zoom The requested zoom level.
     * *
     * @return `true` if the requested zoom level is supported by this
     * *         provider.
     */
    fun isZoomLevelAvailable(zoom: Int): Boolean {
        return zoom >= this.minimumZoom && zoom <= this.maximumZoom
    }

    // ------------------------------------------------------------------------
    // Private Methods
    // ------------------------------------------------------------------------

    private fun calculateZoomConstraints() {
        if (this.isDatabaseAvailable) {
            val projection = arrayOf("value")

            val minArgs = arrayOf("minzoom")

            val maxArgs = arrayOf("maxzoom")

            var c: Cursor

            c = this.mDatabase!!.query("metadata", projection, "name = ?", minArgs, null, null, null)

            c.moveToFirst()
            if (!c.isAfterLast()) {
                this.minimumZoom = c.getInt(0)
            }
            c.close()

            c = this.mDatabase!!.query("metadata", projection, "name = ?", maxArgs, null, null, null)

            c.moveToFirst()
            if (!c.isAfterLast()) {
                this.maximumZoom = c.getInt(0)
            }
            c.close()
        }
    }

    private fun calculateBounds() {
        if (this.isDatabaseAvailable) {
            val projection = arrayOf("value")

            val subArgs = arrayOf("bounds")

            val c = this.mDatabase!!.query("metadata", projection, "name = ?", subArgs, null, null, null)

            c.moveToFirst()
            if (!c.isAfterLast) {
                val parts = c.getString(0).split(",")

                val w = java.lang.Double.parseDouble(parts[0])
                val s = java.lang.Double.parseDouble(parts[1])
                val e = java.lang.Double.parseDouble(parts[2])
                val n = java.lang.Double.parseDouble(parts[3])

                val ne = LatLng(n, e)
                val sw = LatLng(s, w)

                this.bounds = LatLngBounds(sw, ne)
            }
            c.close()
        }
    }

    private val isDatabaseAvailable: Boolean
        get() = this.mDatabase != null && this.mDatabase!!.isOpen

}