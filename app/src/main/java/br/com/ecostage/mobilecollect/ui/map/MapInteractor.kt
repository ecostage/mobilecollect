package br.com.ecostage.mobilecollect.ui.map

import br.com.ecostage.mobilecollect.listener.OnMapDownloadListener

/**
 * Created by cmaia on 7/22/17.
 */
interface MapInteractor {
    fun loadUserCollects()
    fun downloadOfflineArea(onMapDownloadListener: OnMapDownloadListener)
}