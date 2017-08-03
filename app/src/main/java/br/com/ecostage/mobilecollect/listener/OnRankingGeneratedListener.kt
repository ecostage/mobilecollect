package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.Rank

/**
 * Created by cmaia on 7/31/17.
 */
interface OnRankingGeneratedListener {
    fun onGeneralRankingLoaded(rank: List<Rank>)
    fun onGeneralRankingLoadError()
}