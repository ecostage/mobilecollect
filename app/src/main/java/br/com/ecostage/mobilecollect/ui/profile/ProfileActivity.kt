package br.com.ecostage.mobilecollect.ui.profile

import android.os.Bundle
import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.profile.collect.UserCollectActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.startActivity

/**
 * Activity for profile view.
 *
 * Created by andremaia on 7/18/17.
 */
class ProfileActivity : BottomNavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileMyCollects.setOnClickListener {
            startActivity<UserCollectActivity>()
        }
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_profile
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_profile
    }
}