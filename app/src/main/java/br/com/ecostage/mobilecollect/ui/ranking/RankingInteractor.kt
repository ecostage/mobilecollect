package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnGeneralRankingLoadedListener
import br.com.ecostage.mobilecollect.listener.OnTeamRankingLoadedListener

/**
 * Created by cmaia on 7/30/17.
 */
interface RankingInteractor {
    fun findGeneralRanking(onGeneralRankingLoadedListener: OnGeneralRankingLoadedListener)
    fun findTeamsRanking(onTeamRankingLoadedListener: OnTeamRankingLoadedListener)
}