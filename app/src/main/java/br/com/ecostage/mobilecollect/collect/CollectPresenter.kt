package br.com.ecostage.mobilecollect.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectPresenter {
    fun takePhoto()
    fun onPermissionsNeeded()
    fun onPermissionDenied(message: String)
}