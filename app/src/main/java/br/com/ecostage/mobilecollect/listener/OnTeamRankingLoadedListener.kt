package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.TeamRanking

/**
 * Created by cmaia on 7/31/17.
 */
interface OnTeamRankingLoadedListener {
    fun onTeamRankingLoaded(teamsRanking: List<TeamRanking>)
    fun onTeamRankingLoadedError()
}