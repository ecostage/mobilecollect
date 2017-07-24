package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.OnCollectLoadedListener

/**
 * Created by cmaia on 7/23/17.
 */
interface CollectRepository {
    fun loadCollectsByUser(userId: String, onCollectLoadedListener: OnCollectLoadedListener)
}