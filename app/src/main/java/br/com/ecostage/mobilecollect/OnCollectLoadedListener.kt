package br.com.ecostage.mobilecollect

import br.com.ecostage.mobilecollect.model.Collect

/**
 * Created by cmaia on 7/23/17.
 */
interface OnCollectLoadedListener {
    fun onCollectLoaded(collect: Collect)
    fun onCollectLoadedError()
}