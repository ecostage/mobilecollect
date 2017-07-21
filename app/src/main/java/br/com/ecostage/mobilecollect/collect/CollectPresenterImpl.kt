package br.com.ecostage.mobilecollect.collect

import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView) : CollectPresenter {

    private val collectInteractor : CollectInteractor = CollectInteractorImpl()

    override fun decompressMapSnapshot(compressSnapshot: ByteArray): Bitmap =
            BitmapFactory.decodeByteArray(compressSnapshot, 0, compressSnapshot.size)

    override fun takePhoto() = collectView.showCamera()

    override fun onPermissionsNeeded() = collectView.showRequestPermissionsDialog()

    override fun onPermissionDenied(message: String) = collectView.showMessageAsLongToast(message)

    override fun save(collect: Collect) {
        collectView.showProgress()

        if (collect.userId != null) {
            val collect = collectInteractor.save(collect)
            collectView.hideProgress()
            collectView.showCollectRequestSuccess()
            collectView.returnToMap(collect)
        } else {
            collectView.hideProgress()
            collectView.showNoUserError()
        }
    }
}