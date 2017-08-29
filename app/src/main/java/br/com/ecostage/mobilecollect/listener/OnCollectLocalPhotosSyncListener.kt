package br.com.ecostage.mobilecollect.listener

import com.google.firebase.storage.UploadTask

/**
 * Created by andremaia on 8/28/17.
 */
interface OnCollectLocalPhotosSyncListener {
    fun onCollectLocalPhotosSyncStarted()
    fun onCollectLocalPhotosSynced(result: UploadTask.TaskSnapshot?)
    fun onCollectLocalPhotosSyncError()
}