package br.com.ecostage.mobilecollect.ui.profile

/**
 * Created by andremaia on 8/2/17.
 */
class ProfilePresenterImpl(var view: ProfileView) :
        ProfilePresenter,
        ProfileInteractor.OnPasswordResetResult {

    private val profileInteractor: ProfileInteractor = ProfileInteractorImpl(this)

    override fun resetPasswordRequest() {
        view.showProgress()
        profileInteractor.requestResetPasswordToFirebase()
    }

    override fun onPasswordResetSuccess(emailAddress: String) {
        view.hideProgress()
        view.resetPasswordRequestWithSuccess(emailAddress)
    }

    override fun onPasswordResetError(emailAddress: String) {
        view.hideProgress()
        view.resetPasswordRequestWithError(emailAddress)
    }

    override fun onPasswordResetUserIsNotUsingEmailAuth() {
        view.hideProgress()
        view.resetPasswordRequestUserNotUsingEmailAndPassword()
    }

}