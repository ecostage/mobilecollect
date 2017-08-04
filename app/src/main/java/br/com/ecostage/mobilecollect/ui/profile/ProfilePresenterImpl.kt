package br.com.ecostage.mobilecollect.ui.profile

import br.com.ecostage.mobilecollect.listener.OnUserLoadedWithoutScoreListener
import br.com.ecostage.mobilecollect.model.Team
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor

/**
 * Created by andremaia on 8/2/17.
 */
class ProfilePresenterImpl(var view: ProfileView) :
        ProfilePresenter,
        ProfileInteractor.OnPasswordResetResult,
        ProfileInteractor.OnLoadTotalCollectsFromUser,
        CollectInteractor.OnTeamListListener,
        ProfileInteractor.OnUserSignOutListener,
        OnUserLoadedWithoutScoreListener {

    private val profileInteractor: ProfileInteractor = ProfileInteractorImpl(
            this,
            this,
            this,
            this,
            this)

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


    override fun loadTotalCollectsFromUser() {
        profileInteractor.loadTotalCollectsFromUser()
    }

    override fun loadTotalScoresFromUser() {

    }

    override fun loadTeamsListFromUser() {
        profileInteractor.loadTeamsListFromUser()
    }

    override fun onLoadTotalCollectsFromUser(total: Long) {
        view.setTotalCollectsForUser(total)
    }

    override fun onTeamListReady(teams: Array<Team>) {

        val teamsString = teams
                .map { it.name }
                .joinToString("; ")

        view.setUserTeams(teamsString)
    }

    override fun onTeamHasNoTeams() {

    }

    override fun onTeamListError() {

    }

    override fun signOut() {
        profileInteractor.signOut()
    }

    override fun onUserSignOut() {
        view.signOut()
    }

    override fun loadCurrentUser() {
        profileInteractor.loadCurrentUser()
    }

    override fun onUserLoaded(user: User) {
        view.setCurrentUser(user.email)
    }

    override fun onUserLoadingError() {
        view.setCurrentUserOnError()
    }
}