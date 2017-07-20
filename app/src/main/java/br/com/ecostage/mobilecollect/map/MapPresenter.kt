package br.com.ecostage.mobilecollect.map

import android.graphics.Bitmap

interface MapPresenter {
    fun mark(latitude: Double, longitude: Double)
    fun takeMapSnapshot()
    fun onPermissionNeeded()
    fun onPermissionDenied(message: String)
    fun collect(latitude: Double, longitude: Double, compressedMapSnapshot: ByteArray)
    fun compressMapSnapshot(mapSnapshot : Bitmap?): ByteArray
}