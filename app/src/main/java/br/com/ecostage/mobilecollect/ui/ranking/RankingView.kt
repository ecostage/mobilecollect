package br.com.ecostage.mobilecollect.ui.ranking

/**
 * Created by cmaia on 7/29/17.
 */
interface RankingView {
    fun populateUserPoints(userRankingDetailsViewModel: UserRankingDetailsViewModel)
    fun populateGeneralRanking(userRankingDetailsViewModel: List<UserRankingDetailsViewModel>)
    fun populateTeamsRanking(userTeamRankingViewModel: List<UserTeamRankingViewModel>)
    fun showProgress()
    fun hideProgress()
}