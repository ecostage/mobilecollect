package br.com.ecostage.mobilecollect.ui.login

import com.google.firebase.auth.AuthCredential

class LoginPresenterImpl(val view: LoginView,
                         activity: LoginActivity,
                         googleApiToken: String)
    : LoginPresenter,
        LoginInteractor.OnEmailSignInFinishedListener,
        LoginInteractor.OnSignInWithGoogleFinishedListener {

    private val loginInteractor: LoginInteractor = LoginInteractorImpl(this,
            this,
            activity, googleApiToken)

    override fun onCreate() {
        loginInteractor.createListener()
    }

    override fun onStart() {
        loginInteractor.startListener()
    }


    override fun onStop() {
        loginInteractor.stopListener()
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateCredentials(username: String, password: String) {
        this.view.showProgress()
        this.loginInteractor.signInWithEmail(username, password, this)
    }

    override fun onUsernameError() {
        view.setUsernameError()
        view.hideProgress()
    }

    override fun onPasswordError() {
        view.setPasswordError()
        view.hideProgress()
    }

    override fun onSuccess() {
        view.navigateToHome()
    }

    override fun onFailureWithEmail() {
        view.showSignInWithEmailFailure()
    }

    override fun onFailureWithGoogle() {
        view.showSignInWithFailure()
    }

    override fun onConnectionFailedWithGoogle() {
        view.showInternetUnavailableFailure()
    }

    override fun validateGoogleCredentials() {
        this.loginInteractor.signInWithGoogle(this)
    }

    override fun signInWithCredential(credential: AuthCredential) {
        this.loginInteractor.signInWithCredential(credential, this)
    }
}
