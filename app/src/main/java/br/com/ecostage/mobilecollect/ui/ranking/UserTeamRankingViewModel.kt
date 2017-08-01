package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.ui.collect.TeamViewModel

/**
 * Created by cmaia on 7/31/17.
 */
data class UserTeamRankingViewModel(val team: TeamViewModel, val usersRanking: List<UserRankingDetailsViewModel>)