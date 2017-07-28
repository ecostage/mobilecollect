package br.com.ecostage.mobilecollect.ui.collect

import br.com.ecostage.mobilecollect.model.Collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectInteractor {

    interface OnSaveCollectListener {
        fun onSaveCollect(collect: Collect)
        fun onSaveCollectError()
    }

    fun save(collect: Collect, photoBytes: ByteArray)
    fun loadCollect(collectId: String)
}