package br.com.ecostage.mobilecollect.collect

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView) : CollectPresenter {
    override fun takePhoto() {
        collectView.showCamera()
    }

    override fun onPermissionsNeeded() {
        collectView.showRequestPermissionsDialog()
    }

    override fun onPermissionDenied(message: String) {
        collectView.showMessageAsLongToast(message)
    }
}