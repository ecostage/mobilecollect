package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView,
                           val collectActivity: CollectActivity)
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
                collect.date,
                collect.photo.toString()))
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
        // Transform photo to bytearray
        val inputStream = collectActivity.contentResolver.openInputStream(photo)

        collectInteractor.save(collect, toBytes(inputStream))
    }

    private fun toBytes(inputStream: InputStream) : ByteArray {
        val bufferStream = ByteArrayOutputStream()

        inputStream.use { input ->
            bufferStream.use { output ->
                input.copyTo(output)
            }
        }

        return bufferStream.toByteArray()
    }

    override fun onSaveCollect(collect: Collect) {
        collectView.hideProgress()
        collectView.showCollectRequestSuccess()
        collectView.returnToMap(CollectViewModel(collect.id, collect.name, collect.latitude,
                collect.longitude, collect.classification, collect.userId, collect.date,
                photo = collect.photo.toString()))
    }

    override fun onSaveCollectError() {
            collectView.hideProgress()
            collectView.showNoUserError()
    }
}
