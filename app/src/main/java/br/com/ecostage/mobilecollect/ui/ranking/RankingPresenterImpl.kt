package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnGeneralRankingLoadedListener
import br.com.ecostage.mobilecollect.listener.OnTeamRankingLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserLoadedListener
import br.com.ecostage.mobilecollect.model.TeamRanking
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.model.UserRank
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import br.com.ecostage.mobilecollect.ui.collect.TeamViewModel

/**
 * Created by cmaia on 7/29/17.
 */
class RankingPresenterImpl(val rankingView: RankingView) : RankingPresenter {
    private val rankingInteractor : RankingInteractor = RankingInteractorImpl()

    private val userRepository: UserRepository = UserRepositoryImpl()
    override fun loadUserPoints() {
//        rankingView.showUserPointsProgress()
        userRepository.getCurrentUser(object : OnUserLoadedListener {
            override fun onUserLoaded(user: User) {
                rankingView.populateUserPoints(UserRankingDetailsViewModel(user.id, user.email, user.rankingPoints))
//                rankingView.hideUserPointsProgress()
            }

            override fun onUserLoadingError() {
//                rankingView.hideUserPointsProgress()
            }

        })
    }

    override fun loadGeneralRanking() {
//        rankingView.showUserRankingProgress()
        rankingInteractor.findGeneralRanking(object : OnGeneralRankingLoadedListener {
            override fun onGeneralRankingLoaded(generalRank: List<UserRank>) {
                rankingView.populateGeneralRanking(generalRank.mapNotNull {
                    UserRankingDetailsViewModel(it.userId, it.userEmail, it.points)
                }.toList())
                //        rankingView.hideUserRankingProgress()
            }

            override fun onGeneralRankingLoadError() {
                //        rankingView.hideUserRankingProgress()
            }
        })
    }

    override fun loadTeamRanking() {
//        rankingView.showUserTeamRankingProgress()
        rankingInteractor.findTeamsRanking(object : OnTeamRankingLoadedListener {
            override fun onTeamRankingLoaded(teamsRanking: List<TeamRanking>) {
                val teamRankingViewModel = teamsRanking.mapNotNull {
                    val teamViewModel = TeamViewModel()
                    teamViewModel.id = it.team.id
                    teamViewModel.name = it.team.name

                    val users = it.users.mapNotNull { UserRankingDetailsViewModel(it.userId, it.userEmail, it.points) }

                    UserTeamRankingViewModel(teamViewModel, users)
                }

                rankingView.populateTeamsRanking(teamRankingViewModel)
                //        rankingView.hideUserTeamRankingProgress()
            }

            override fun onTeamRankingLoadedError() {
                //        rankingView.hideUserTeamRankingProgress()
            }
        })
    }
}
