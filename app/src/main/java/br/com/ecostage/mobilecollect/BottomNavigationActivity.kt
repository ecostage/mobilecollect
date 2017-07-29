package br.com.ecostage.mobilecollect

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.com.ecostage.mobilecollect.ui.map.MapActivity
import br.com.ecostage.mobilecollect.ui.profile.ProfileActivity
import br.com.ecostage.mobilecollect.ui.ranking.base.RankingActivity


/**
 * Base class for activities that use {@link BottomNavigationView}.
 *
 * Created by andremaia on 7/18/17.
 */
abstract class BottomNavigationActivity : AppCompatActivity(),
        BottomNavigationView.OnNavigationItemSelectedListener {

    private val navigationView: BottomNavigationView
        get() {
            val navigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView
            return navigationView
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId())

        navigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        updateNavigationBarState()
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    public override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.postDelayed({
            when (item.itemId) {
                R.id.action_map -> {
                    startActivity(Intent(this, MapActivity::class.java))

                }
                R.id.action_ranking -> {
                    startActivity(Intent(this, RankingActivity::class.java))
                }
                R.id.action_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            }
            finish()
        }, 300)

        return true
    }

    private fun updateNavigationBarState() {
        val actionId = getNavigationMenuItemId()
        selectBottomNavigationBarItem(actionId)
    }

    fun selectBottomNavigationBarItem(itemId: Int) {
        val menu = navigationView.menu
        var i = 0
        val size = menu.size()
        while (i < size) {
            val item = menu.getItem(i)
            val shouldBeChecked = item.itemId == itemId
            if (shouldBeChecked) {
                item.isChecked = true
                break
            }
            i++
        }
    }

    internal abstract fun getContentViewId(): Int

    internal abstract fun getNavigationMenuItemId(): Int
}