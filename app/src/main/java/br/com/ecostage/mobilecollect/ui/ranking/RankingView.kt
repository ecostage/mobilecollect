package br.com.ecostage.mobilecollect.ui.ranking

/**
 * Created by cmaia on 7/29/17.
 */
interface RankingView {
    fun populateUserPoints(userRankingDetailsViewModel: UserRankingDetailsViewModel)
    fun populateRanking(rankingViewModel: List<RankingViewModel>)
    fun populateTeamsRanking(userTeamRankingViewModel: List<UserTeamRankingViewModel>)
    fun showProgress()
    fun hideProgress()
}