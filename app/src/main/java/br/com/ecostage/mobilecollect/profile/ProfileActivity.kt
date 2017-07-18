package br.com.ecostage.mobilecollect.profile

import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R

/**
 * Activity for profile view.
 *
 * Created by andremaia on 7/18/17.
 */
class ProfileActivity : BottomNavigationActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_profile
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_profile
    }
}