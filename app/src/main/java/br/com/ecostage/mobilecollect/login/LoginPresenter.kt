package br.com.ecostage.mobilecollect.login

import com.google.firebase.auth.AuthCredential

/**
 * Notifications and communication between view and presenter
 *
 * Created by andremaia on 7/13/17.
 */
interface LoginPresenter {
    fun validateCredentials(username: String, password: String)
    fun onCreate()
    fun onDestroy()
    fun onStart()
    fun onStop()
    fun validateGoogleCredentials()
    fun signInWithCredential(credential: AuthCredential)
}