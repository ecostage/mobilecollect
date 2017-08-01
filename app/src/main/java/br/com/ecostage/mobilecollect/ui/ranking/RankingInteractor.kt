package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.OnGeneralRankingLoadedListener
import br.com.ecostage.mobilecollect.OnTeamRankingLoadedListener

/**
 * Created by cmaia on 7/30/17.
 */
interface RankingInteractor {
    fun findGeneralRanking(onGeneralRankingLoadedListener: OnGeneralRankingLoadedListener)
    fun findTeamsRanking(onTeamRankingLoadedListener: OnTeamRankingLoadedListener)
}