package br.com.ecostage.mobilecollect.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
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

    companion object {
        private val WRITE_PERMISSION_CODE = 456
    }

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

        downloadOfflineMapArea.setOnClickListener {
            if (canWriteFiles()) {
                presenter.downloadOfflineArea()
            } else {
                presenter.onPermissionNeeded()
            }
        }

//        manageOfflineMapLinearLayout.setOnClickListener {
//            startActivity<ManageOfflineMapActivity>()
//        }

        profileChangePassword.setOnClickListener {
            presenter.resetPasswordRequest()
        }

        profileSignOut.setOnClickListener {
            presenter.signOut()
        }
    }

    private fun loadData() {
        presenter.loadCurrentUser()
        presenter.loadTotalCollectsFromUser()
        presenter.loadTotalScoresFromUser()
        presenter.loadTeamsListFromUser()
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

    override fun showMapDownloadSuccess() {
        longToast(R.string.map_downloaded_message)
    }

    override fun showMapDownloadFailure() {
        longToast(R.string.map_download_failure_message)
    }

    override fun showMenuBar() {
        bottom_navigation.visibility = View.VISIBLE
    }

    override fun showMapDownloadProgress() {
        this.showProgress()
        mapProgressText.visibility = View.VISIBLE
    }

    override fun hideMapDownloadProgress() {
        this.hideProgress()
        mapProgressText.visibility = View.GONE
    }

    override fun hideMenuBar() {
        bottom_navigation.visibility = View.GONE
    }

    override fun disableScreenTimeout() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun canWriteFiles(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun showRequestPermissionsDialog() {
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_PERMISSION_CODE)
    }

    override fun showMessageAsLongToast(message: String) {
        longToast(message)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_PERMISSION_CODE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    presenter.onPermissionDenied(resources.getString(R.string.download_map_permission_needed))
                } else {
                    presenter.downloadOfflineArea()
                }
        }
    }

    private fun userScoreFormatted(userScore: Int?) = resources.getString(R.string.profile_user_score_format, userScore?.toString())

    private fun defaultUserScore() = resources.getString(R.string.profile_user_score_format, getString(R.string.profile_default_user_score))

    override fun updateMapDownloadProgress(progress: Float) {
        if (progress <= 0f) {
            mapProgressText.text = getString(R.string.map_downloading_message, 0f)
        } else {
            mapProgressText.text = getString(R.string.map_downloading_message, progress)
        }
    }
}
