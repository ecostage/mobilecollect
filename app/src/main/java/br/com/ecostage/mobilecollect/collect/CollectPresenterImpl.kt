package br.com.ecostage.mobilecollect.collect

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.auth.FirebaseAuth

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

    override fun save(name: String, latitude: Double, longitude: Double) {
        collectView.showProgress()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val collect = collectInteractor.save(Collect(name = name,
                    latitude = latitude,
                    longitude = longitude,
                    classification = "Floresta Densa",
                    userId = userId))

            collectView.hideProgress()
            collectView.showCollectRequestSuccess()
            collectView.returnToMap(collect)
        } else {
            collectView.hideProgress()
            collectView.showNoUserError()
        }
    }
}