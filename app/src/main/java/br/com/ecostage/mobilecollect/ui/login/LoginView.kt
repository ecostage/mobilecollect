package br.com.ecostage.mobilecollect.ui.login

/**
 * Represents Login View operations
 *
 * Created by andremaia on 7/13/17.
 */
interface LoginView {
    fun showProgress()
    fun hideProgress()
    fun setUsernameError()
    fun setPasswordError()
    fun navigateToHome()
    fun showSignInWithFailure()
    fun showSignInWithEmailFailure()
}