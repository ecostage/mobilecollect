package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.CollectAvailable

/**
 * Created by andremaia on 8/20/17.
 */
interface OnCollectAvailableLoadedListener {
    fun onCollectAvailableLoaded(collectAvailable: CollectAvailable)
    fun onCollectAvailableLoadedError()
    fun onCollectAvailableLoadedAndNoCollectsFound()
}