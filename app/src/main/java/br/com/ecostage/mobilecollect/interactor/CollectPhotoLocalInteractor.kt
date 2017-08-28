package br.com.ecostage.mobilecollect.interactor

import br.com.ecostage.mobilecollect.listener.OnCollectLocalPhotosQueueSizeReadListener
import br.com.ecostage.mobilecollect.listener.OnCollectLocalPhotosSyncListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import java.io.File

/**
 * Created by andremaia on 8/28/17.
 */
class CollectPhotoLocalInteractor(private val onCollectLocalPhotosQueueSizeReadListener: OnCollectLocalPhotosQueueSizeReadListener,
                                  private val onCollectLocalPhotosSyncListener: OnCollectLocalPhotosSyncListener) {

    private val collectRepo: CollectRepository = CollectRepositoryImpl()

    companion object {
        var COLLECT_PHOTO_PATH: File? = null
    }

    fun loadTotalPhotosNotSynced() {
        if (COLLECT_PHOTO_PATH != null && COLLECT_PHOTO_PATH!!.exists()) {
            val file = COLLECT_PHOTO_PATH!!
            onCollectLocalPhotosQueueSizeReadListener.onCollectLocalPhotosQueueSizeRead(file.listFiles().size)
        }
    }

    fun syncCollectedData() {
        collectRepo.syncImagesToCloud(onCollectLocalPhotosSyncListener)
    }
}