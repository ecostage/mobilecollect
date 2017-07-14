package br.com.ecostage.mobilecollect.login

import com.google.firebase.auth.AuthCredential

/**
 * Responsible for interact view sign in model.
 *
 * Created by andremaia on 7/13/17.
 */
interface LoginInteractor {

    interface OnEmailSignInFinishedListener {
        fun onUsernameError()
        fun onPasswordError()
        fun onSuccess()
        fun onFailureWithEmail()
    }

    interface OnSignInWithGoogleFinishedListener {
        fun onFailureWithGoogle()
    }

    fun signInWithEmail(username: String, password: String, listener: OnEmailSignInFinishedListener)
    fun startListener()
    fun stopListener()
    fun signInWithGoogle(listener: OnSignInWithGoogleFinishedListener)
    fun createListener()
    fun signInWithCredential(credential: AuthCredential, listener: OnSignInWithGoogleFinishedListener)


}