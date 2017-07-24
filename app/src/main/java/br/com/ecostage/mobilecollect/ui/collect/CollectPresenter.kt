package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap
import java.util.*

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectPresenter {
    fun takePhoto()
    fun decompressMapSnapshot(compressSnapshot: ByteArray) : Bitmap
    fun onPermissionsNeeded()
    fun onPermissionDenied(message: String)
    fun save(name: String, latitude: Double, longitude: Double, classification: String, date: Date)
    fun loadCollect(collectId: String)
}