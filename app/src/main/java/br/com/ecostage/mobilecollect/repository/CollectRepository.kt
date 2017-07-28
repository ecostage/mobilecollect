package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor

/**
 * Created by cmaia on 7/23/17.
 */
interface CollectRepository {
    fun save(userId: String, collect: Collect, photoBytes: ByteArray, onCollectSaveListener: CollectInteractor.OnSaveCollectListener)
    fun loadCollectsByUser(userId: String, onCollectLoadedListener: OnCollectLoadedListener)
    fun loadCollect(collectId: String, onCollectLoadedListener: OnCollectLoadedListener)
}