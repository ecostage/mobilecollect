package br.com.ecostage.mobilecollect.ui.map

import android.content.res.AssetManager
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Created by cmaia on 8/16/17.
 */
class CustomMapTileProvider(val assets : AssetManager) : TileProvider {

    companion object {
        val TILE_WIDTH : Int = 256
        val TILE_HEIGHT : Int = 256
        val BUFFER_SIZE : Int = 16 * 1024
    }

    override fun getTile(x: Int, y: Int, zoom: Int): Tile {
        val image: ByteArray = readTileImage(x, y, zoom)
        return Tile(TILE_WIDTH, TILE_HEIGHT, image)
    }

    fun readTileImage(x: Int, y: Int, zoom: Int) : ByteArray {
        val input: InputStream = assets.open(getTileFilename(x, y, zoom))
        val buffer: ByteArrayOutputStream = ByteArrayOutputStream()

        val data = ByteArray(BUFFER_SIZE)

        input.bufferedReader().use { buffer.write(data, 0, it.read()) }

        buffer.flush()

        return buffer.toByteArray()
    }

    private fun getTileFilename(x: Int, y: Int, zoom: Int): String {
        return "map/$zoom/$x/$y.png"
    }
}