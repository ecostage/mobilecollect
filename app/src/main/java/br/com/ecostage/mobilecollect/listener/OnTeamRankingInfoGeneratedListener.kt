package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.Rank

/**
 * Created by cmaia on 7/31/17.
 */
interface OnTeamRankingInfoGeneratedListener {
    fun onTeamRankingInfoLoaded(rank: List<Rank>)
    fun onTeamRankingInfoLoadError()
}