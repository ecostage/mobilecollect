package br.com.ecostage.mobilecollect.ui.ranking.base

import android.os.Bundle
import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R

/**
 * Activity for ranking view.
 *
 * Created by andremaia on 7/18/17.
 */
class RankingActivity : BottomNavigationActivity(), RankingView {

    private val rankingPresenter : RankingPresenter = RankingPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    private fun setupUi() {

    }

    override fun getContentViewId(): Int {
        return R.layout.activity_ranking
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_ranking
    }



}