package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnTeamRankingInfoGeneratedListener
import br.com.ecostage.mobilecollect.listener.OnTeamRankingLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserGeneralRankingInfoLoadedListener

/**
 * Created by cmaia on 7/30/17.
 */
interface RankingInteractor {
    fun findGeneralRanking(onTeamRankingInfoGeneratedListener: OnTeamRankingInfoGeneratedListener)
    fun generateRanking(onTeamRankingInfoGeneratedListener: OnTeamRankingInfoGeneratedListener)
    fun loadUserGeneralRankingInfo(onUserGeneralRankingInfoLoadedListener: OnUserGeneralRankingInfoLoadedListener)
    fun findTeamsRanking(onTeamRankingLoadedListener: OnTeamRankingLoadedListener)
}