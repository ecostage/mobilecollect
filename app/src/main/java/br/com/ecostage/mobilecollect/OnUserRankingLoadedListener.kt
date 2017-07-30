package br.com.ecostage.mobilecollect

/**
 * Created by cmaia on 7/30/17.
 */
interface OnUserRankingLoadedListener {
    fun onRankingLoaded(userPoints: Int?)
    fun onRankingLoadingError()
}