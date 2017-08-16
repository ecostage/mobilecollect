package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.Rank

/**
 * Created by cmaia on 8/2/17.
 */
interface OnUserGeneralRankingInfoLoadedListener {
    fun onUserGeneralRankingInfoLoaded(rank: List<Rank>)
    fun onUserGeneralRankingInfoLoadError()
}