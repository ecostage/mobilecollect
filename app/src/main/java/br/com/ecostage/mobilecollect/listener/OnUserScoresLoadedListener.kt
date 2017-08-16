package br.com.ecostage.mobilecollect.listener

/**
 * Created by cmaia on 7/30/17.
 */
interface OnUserScoresLoadedListener {
    fun onRankingLoaded(userPoints: Int?)
    fun onRankingLoadingError()
}