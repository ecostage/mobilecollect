package br.com.ecostage.mobilecollect.map

import br.com.ecostage.mobilecollect.collect.Collect

/**
 * Created by cmaia on 7/22/17.
 */
interface MapInteractor {

    interface OnCollectLoadedListener {
        fun onCollectLoaded(collect: Collect)
        fun onCollectLoadedError()
    }

    fun loadUserCollects()
}