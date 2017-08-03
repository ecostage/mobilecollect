package br.com.ecostage.mobilecollect.ui.ranking

import android.os.Bundle
import android.view.View
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
//        rankingPresenter.loadGeneralRankingInfo()
//        rankingPresenter.loadTeamRankingInfo()
        rankingPresenter.loadRanking()
//        rankingPresenter.loadGeneralRanking()
//        rankingPresenter.loadTeamRanking()
    }

    override fun populateUserPoints(userRankingDetailsViewModel: UserRankingDetailsViewModel) {
        userRankingDetails = userRankingDetailsViewModel

        rankingUserPoint.text = userRankingDetailsViewModel.userRankingPoints.toString()
    }

    override fun populateRanking(rankingViewModel: List<RankingViewModel>) {
        if (rankingViewModel.isNotEmpty()) {
            rankingListEmptyData.visibility = View.GONE
            val adapter = RankingListAdapter(this, rankingViewModel)
            rankingList.adapter = adapter
        }
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