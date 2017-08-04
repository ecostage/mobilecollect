package br.com.ecostage.mobilecollect.ui.profile

/**
 * Created by andremaia on 8/2/17.
 */
interface ProfileInteractor {

    interface OnPasswordResetResult {
        fun onPasswordResetSuccess(emailAddress: String)
        fun onPasswordResetError(emailAddress: String)
        fun onPasswordResetUserIsNotUsingEmailAuth()
    }

    interface OnLoadTotalCollectsFromUser {
        fun onLoadTotalCollectsFromUser(total: Long)
    }

    interface OnUserSignOutListener {
        fun onUserSignOut()
    }

    fun requestResetPasswordToFirebase()
    fun loadTotalCollectsFromUser()
    fun loadTeamsListFromUser()
    fun signOut()
    fun loadCurrentUser()
}