package br.com.ecostage.mobilecollect.ui.profile

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import br.com.ecostage.mobilecollect.BottomNavigationActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.helper.ProgressBarHandler
import br.com.ecostage.mobilecollect.ui.login.LoginActivity
import br.com.ecostage.mobilecollect.ui.profile.collect.UserCollectActivity
import br.com.ecostage.mobilecollect.ui.ranking.RankingActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

/**
 * Activity for profile view.
 *
 * Created by andremaia on 7/18/17.
 */
class ProfileActivity :
        BottomNavigationActivity(),
        ProfileView {


    private val presenter: ProfilePresenter = ProfilePresenterImpl(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewControls()
        loadData()
    }

    private fun setupViewControls() {
        totalCollectLinearLayout.setOnClickListener {
            startActivity<UserCollectActivity>()
        }

        totalPointsLinearLayout.setOnClickListener {
            startActivity<RankingActivity>()
        }

        profileChangePassword.setOnClickListener {
            presenter.resetPasswordRequest()
        }

        profileSignOut.setOnClickListener {
            presenter.signOut()
        }

        profileSyncData.setOnClickListener {
            if (isNetworkAvailable()) {
                profileSyncData.isEnabled = false
                presenter.syncCollectedData()
            } else {
                longToast(R.string.profile_message_sync_no_networking_available)
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun loadData() {
        presenter.loadCurrentUser()
        presenter.loadTotalCollectsFromUser()
        presenter.loadTotalScoresFromUser()
        presenter.loadTeamsListFromUser()
        presenter.loadDataToSync()
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_profile
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_profile
    }

    override fun resetPasswordRequestWithSuccess(emailAddress: String) {
        android.app.AlertDialog
                .Builder(this)
                .setTitle(getString(R.string.dialog_title_reset_password))
                .setMessage(getString(R.string.dialog_message_reset_password_with_success, emailAddress))
                .setNegativeButton(android.R.string.ok) { _, _ -> }
                .create()
                .show()

    }

    override fun showProgress() {
        ProgressBarHandler().showProgress(true, containerScrollView, progressBar)
    }

    override fun hideProgress() {
        ProgressBarHandler().showProgress(false, containerScrollView, progressBar)
    }

    override fun resetPasswordRequestWithError(emailAddress: String) {
        Snackbar.make(containerScrollView,
                R.string.message_snackbar_reset_password_request_error,
                Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun resetPasswordRequestUserNotUsingEmailAndPassword() {
        android.app.AlertDialog
                .Builder(this)
                .setTitle(getString(R.string.dialog_title_reset_password))
                .setMessage(getString(R.string.dialog_message_reset_password_not_using_email_and_password))
                .setNegativeButton(android.R.string.ok) { _, _ -> }
                .create()
                .show()
    }

    override fun setTotalCollectsForUser(total: Long) {
        totalCollectsTextView.text = total.toString()
    }

    override fun setUserTeams(teams: String) {
        userTeamsTextView.text = teams
    }

    override fun signOut() {
        startActivity<LoginActivity>()
        finish()
    }

    override fun setCurrentUser(email: String) {
        userEmailTextView.text = email
    }

    override fun setCurrentUserOnError() {
        userEmailTextView.text = getString(R.string.profile_default_user_email_address)
    }

    override fun setUserScore(userScore: Int?) {
        userScoreTextView.text = userScoreFormatted(userScore) ?: defaultUserScore()
    }

    override fun setUserScoreOnError() {
        userScoreTextView.text = defaultUserScore()
    }

    private fun userScoreFormatted(userScore: Int?) = resources.getString(R.string.profile_user_score_format, userScore?.toString())

    private fun defaultUserScore() = resources.getString(R.string.profile_user_score_format, getString(R.string.profile_default_user_score))

    override fun setSyncStatus(size: Int) {
        when (size) {
            0 -> applyAllDataSynced()
            else -> applyDataToSync(size)
        }
    }

    private fun applyAllDataSynced() {
        syncStarted()
        userSyncDataInfo.text = getString(R.string.default_message_user_all_data_synced)
    }

    private fun syncStarted() {
        profileSyncData.isEnabled = false
    }

    private fun applyDataToSync(size: Int) {
        profileSyncData.isEnabled = isNetworkAvailable()
        userSyncDataInfo.text = getString(R.string.profile_message_collects_to_sync, size)
    }

    override fun collectSyncStarted() {
        syncStarted()
    }

    override fun showSuccessMessageSyncedData() {
        presenter.loadDataToSync()
        longToast(R.string.profile_message_sync_with_success)
    }

    override fun showFailMessageSyncedData() {
        longToast(R.string.profile_message_sync_fail)
    }

}
