package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.Collect

/**
 * Created by cmaia on 7/23/17.
 */
interface OnCollectLoadedListener {
    fun onCollectLoaded(collect: Collect)
    fun onCollectImageLoaded(collect: Collect)
    fun onCollectLoadedError()
    fun onCollectImageLoadedError()
}