package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView)
    : CollectPresenter, CollectInteractor.OnSaveCollectListener {
    private val collectInteractor : CollectInteractor = CollectInteractorImpl(this)

    override fun decompressMapSnapshot(compressSnapshot: ByteArray): Bitmap =
            BitmapFactory.decodeByteArray(compressSnapshot, 0, compressSnapshot.size)

    override fun takePhoto() = collectView.showCamera()

    override fun onPermissionsNeeded() = collectView.showRequestPermissionsDialog()

    override fun onPermissionDenied(message: String) = collectView.showMessageAsLongToast(message)

    override fun save(name: String, latitude: Double, longitude: Double, classification: String) {
        collectView.showProgress()

        val collect = Collect()
        collect.name = name
        collect.latitude = latitude
        collect.longitude = longitude
        collect.classification = classification

        collectInteractor.save(collect)
    }

    override fun onSaveCollect(collect: Collect) {
        collectView.hideProgress()
        collectView.showCollectRequestSuccess()
        collectView.returnToMap(CollectViewModel(collect.id, collect.name, collect.latitude, collect.longitude, collect.classification, collect.userId))
    }

    override fun onSaveCollectError() {
            collectView.hideProgress()
            collectView.showNoUserError()
    }
}
