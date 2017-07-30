package br.com.ecostage.mobilecollect.ui.ranking.base

import br.com.ecostage.mobilecollect.OnUserLoadedListener
import br.com.ecostage.mobilecollect.model.User

/**
 * Created by cmaia on 7/29/17.
 */
class RankingPresenterImpl(val rankingView: RankingView)
    : RankingPresenter, OnUserLoadedListener {
    private val rankingInteractor : RankingInteractor = RankingInteractorImpl(this)

    override fun loadUserRankingDetails() {
        rankingInteractor.findUser()
    }

    override fun onUserLoaded(user: User) {
        rankingView.populateUserRankingInfo(UserRankingDetailsViewModel(user.id, user.name, user.rankingPoints))
    }

    override fun onUserLoadingError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}