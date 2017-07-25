package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectPresenter {
    fun takePhoto()
    fun decompressMapSnapshot(compressSnapshot: ByteArray) : Bitmap
    fun onPermissionsNeeded()
    fun onPermissionDenied(message: String)
    fun save(collect: Collect)
    fun loadCollect(collectId: String)
}