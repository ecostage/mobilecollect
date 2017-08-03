package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnRankingGeneratedListener
import br.com.ecostage.mobilecollect.listener.OnTeamRankingLoadedListener

/**
 * Created by cmaia on 7/30/17.
 */
interface RankingInteractor {
    fun findGeneralRanking(onRankingGeneratedListener: OnRankingGeneratedListener)
    fun generateRanking(onRankingGeneratedListener: OnRankingGeneratedListener)
    fun findTeamsRanking(onTeamRankingLoadedListener: OnTeamRankingLoadedListener)
}