package br.com.ecostage.mobilecollect.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectView {
    fun showCamera()
    fun showRequestPermissionsDialog()
    fun canAccessCamera() : Boolean
    fun showMessageAsLongToast(message: String)
    fun showProgress()
    fun hideProgress()
    fun showCollectRequestSuccess()
    fun navigateToMap()
}