package br.com.ecostage.mobilecollect

/**
 * Created by cmaia on 7/30/17.
 */
interface OnUserPointsLoadedListener {
    fun onRankingLoaded(userPoints: Int?)
    fun onRankingLoadingError()
}