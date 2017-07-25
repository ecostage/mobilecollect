package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import br.com.ecostage.mobilecollect.OnCollectLoadedListener

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView)
    : CollectPresenter, CollectInteractor.OnSaveCollectListener, OnCollectLoadedListener {
    private val collectInteractor : CollectInteractor = CollectInteractorImpl(this, this)

    override fun loadCollect(collectId: String) {
        collectInteractor.loadCollect(collectId)
    }

    override fun onCollectLoaded(collect: Collect) {
        collectView.populateFields(CollectViewModel(collect.id,
                collect.name,
                collect.latitude,
                collect.longitude,
                collect.classification,
                collect.userId,
                collect.date))
    }

    override fun onCollectLoadedError() {
        collectView.showMessageAsLongToast("Failed to show collect.")
    }

    override fun decompressMapSnapshot(compressSnapshot: ByteArray): Bitmap =
            BitmapFactory.decodeByteArray(compressSnapshot, 0, compressSnapshot.size)

    override fun takePhoto() = collectView.showCamera()

    override fun onPermissionsNeeded() = collectView.showRequestPermissionsDialog()

    override fun onPermissionDenied(message: String) = collectView.showMessageAsLongToast(message)

    override fun save(collect: Collect) {
        collectView.showProgress()
        collectInteractor.save(collect)
    }

    override fun onSaveCollect(collect: Collect) {
        collectView.hideProgress()
        collectView.showCollectRequestSuccess()
        collectView.returnToMap(CollectViewModel(collect.id, collect.name, collect.latitude, collect.longitude, collect.classification, collect.userId, collect.date))
    }

    override fun onSaveCollectError() {
            collectView.hideProgress()
            collectView.showNoUserError()
    }
}
