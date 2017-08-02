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

    fun requestResetPasswordToFirebase()
}