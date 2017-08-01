package br.com.ecostage.mobilecollect.ui.ranking

import android.os.Bundle
import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_ranking.*

/**
 * Activity for ranking view.
 *
 * Created by andremaia on 7/18/17.
 */
class RankingActivity : BottomNavigationActivity(), RankingView {

    private val rankingPresenter : RankingPresenter = RankingPresenterImpl(this)

    private var userRankingDetails : UserRankingDetailsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rankingPresenter.loadUserPoints()
        rankingPresenter.loadGeneralRanking()
        rankingPresenter.loadTeamRanking()
    }

    override fun populateUserPoints(userRankingDetailsViewModel: UserRankingDetailsViewModel) {
        userRankingDetails = userRankingDetailsViewModel

        rankingUserPoint.text = userRankingDetailsViewModel.userRankingPoints.toString()
    }

    override fun populateGeneralRanking(userRankingDetailsViewModel: List<UserRankingDetailsViewModel>) {
        // show
    }

    override fun populateTeamsRanking(userTeamRankingViewModel: List<UserTeamRankingViewModel>) {
        // show
    }

    override fun showProgress() {
//        ProgressBarHandler().showProgress(true, scrollViewRankingActivity, rankingProgress)
    }

    override fun hideProgress() {
//        ProgressBarHandler().showProgress(false, scrollViewRankingActivity, rankingProgress)
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_ranking
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_ranking
    }
}