package br.com.ecostage.mobilecollect

import br.com.ecostage.mobilecollect.model.UserRank

/**
 * Created by cmaia on 7/31/17.
 */
interface OnGeneralRankingLoadedListener {
    fun onGeneralRankingLoaded(generalRank: List<UserRank>)
    fun onGeneralRankingLoadError()
}