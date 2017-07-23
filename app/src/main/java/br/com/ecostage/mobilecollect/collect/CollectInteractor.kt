package br.com.ecostage.mobilecollect.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectInteractor {

    interface OnSaveCollectListener {
        fun onSaveCollect(collect: Collect)
        fun onSaveCollectError()
    }

    fun save(collect: Collect)
}