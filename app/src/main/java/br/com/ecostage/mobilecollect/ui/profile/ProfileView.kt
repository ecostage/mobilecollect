package br.com.ecostage.mobilecollect.ui.profile

/**
 * Created by andremaia on 8/2/17.
 */
interface ProfileView {

    fun resetPasswordRequestWithSuccess(emailAddress: String)
    fun showProgress()
    fun hideProgress()
    fun resetPasswordRequestWithError(emailAddress: String)
    fun resetPasswordRequestUserNotUsingEmailAndPassword()
    fun setTotalCollectsForUser(total: Long)
    fun setUserTeams(teams: String)
    fun signOut()
    fun setCurrentUser(email: String)
    fun setCurrentUserOnError()
    fun setUserScore(userScore: Int?)
    fun setUserScoreOnError()
    fun setSyncStatus(size: Int)
    fun showSuccessMessageSyncedData()
    fun showFailMessageSyncedData()
    fun collectSyncStarted()
}