package br.com.ecostage.mobilecollect.ui.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectView {
    enum class CollectMode {
        VISUALIZING,
        COLLECTING
    }

    fun showCamera()
    fun showRequestPermissionsDialog()
    fun canAccessCamera() : Boolean
    fun showMessageAsLongToast(message: String)
    fun showProgress()
    fun hideProgress()
    fun showCollectRequestSuccess()
    fun returnToMap(collectViewModel: CollectViewModel?)
    fun showNoUserError()
    fun populateFields(collectViewModel: CollectViewModel)
    fun hideImageContainers()
    fun showImageContainers()
}
