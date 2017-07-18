package br.com.ecostage.mobilecollect.ranking

import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R

/**
 * Activity for ranking view.
 *
 * Created by andremaia on 7/18/17.
 */
class RankingActivity : BottomNavigationActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_ranking
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_ranking
    }
}