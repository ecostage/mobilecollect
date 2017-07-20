package br.com.ecostage.mobilecollect.collect

import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView) : CollectPresenter {
    override fun decompressMapSnapshot(compressSnapshot: ByteArray): Bitmap =
            BitmapFactory.decodeByteArray(compressSnapshot, 0, compressSnapshot.size)

    override fun takePhoto() = collectView.showCamera()

    override fun onPermissionsNeeded() = collectView.showRequestPermissionsDialog()


    override fun onPermissionDenied(message: String) = collectView.showMessageAsLongToast(message)
}