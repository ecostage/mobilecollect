package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectPresenter {
    fun takePhoto()
    fun decompressMapSnapshot(compressSnapshot: ByteArray): Bitmap
    fun onPermissionsNeeded()
    fun onPermissionDenied(message: String)
    fun save(viewModel: CollectViewModel)
    fun loadCollect(collectId: String)
    fun compressCollectPhoto(filePath: String, format: Bitmap.CompressFormat, qualityLevel: Int): ByteArray
    fun setupCollectMode(mode: CollectView.CollectMode)
    fun convertCollectPhoto(img: ByteArray): Bitmap
}