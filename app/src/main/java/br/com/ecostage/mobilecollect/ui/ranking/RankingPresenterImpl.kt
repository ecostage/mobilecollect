package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnTeamRankingInfoGeneratedListener
import br.com.ecostage.mobilecollect.listener.OnUserGeneralRankingInfoLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserLoadedListener
import br.com.ecostage.mobilecollect.model.Rank
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import br.com.ecostage.mobilecollect.ui.collect.TeamViewModel
import br.com.ecostage.mobilecollect.ui.profile.UserViewModel

/**
 * Created by cmaia on 7/29/17.
 */
class RankingPresenterImpl(val rankingView: RankingView) : RankingPresenter {
    private val rankingInteractor : RankingInteractor = RankingInteractorImpl()

    private val userRepository: UserRepository = UserRepositoryImpl()

    override fun loadUserPoints() {
        userRepository.getCurrentUser(object : OnUserLoadedListener {
            override fun onUserLoaded(user: User) {
                rankingView.populateUserPoints(UserRankingDetailsViewModel(user.id, user.email, user.rankingPoints))
            }

            override fun onUserLoadingError() {
            }
        })
    }

    override fun loadUserGeneralRankingInfo() {
        rankingView.showProgress()

        rankingInteractor.loadUserGeneralRankingInfo(object : OnUserGeneralRankingInfoLoadedListener {
            override fun onUserGeneralRankingInfoLoaded(rank: List<Rank>) {
                if (rank.isNotEmpty()) {
                    rankingView.populateUserGeneralRankingInfo(
                            rank.mapNotNull {
                                if (it.user != null) {
                                    RankingViewModel(it.place, UserViewModel(it.user.id, it.user.email), null, it.points)
                                } else {
                                    null
                                }
                            }.toList()
                    )

                    rankingView.hideProgress()
                }
            }

            override fun onUserGeneralRankingInfoLoadError() {
                rankingView.hideProgress()
            }
        })
    }

    override fun loadUserTeamsRankingInfo() {
        //        rankingView.showUserTeamsRankingProgress()
        rankingInteractor.generateRanking(object : OnTeamRankingInfoGeneratedListener {
            override fun onTeamRankingInfoLoaded(rank: List<Rank>) {
                rankingView.populateUserTeamsRankingInfo(rank.mapNotNull {
                    if (it.user != null) {
                        RankingViewModel(it.place, UserViewModel(it.user.id, it.user.email), null, it.points)
                    } else {
                        val team = TeamViewModel()
                        team.id = it.team?.id
                        team.name = it.team?.name
                        RankingViewModel(it.place, null, team, it.points)
                    }

                    //        rankingView.hideUserTeamsRankingProgress()
                }.toList())
                //        rankingView.hideUserTeamsRankingProgress()
            }

            override fun onTeamRankingInfoLoadError() {
                //        rankingView.hideUserRankingProgress()
            }
        })
    }
}
