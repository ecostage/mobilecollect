package br.com.ecostage.mobilecollect.ui.profile

import br.com.ecostage.mobilecollect.listener.OnMapDownloadListener
import br.com.ecostage.mobilecollect.interactor.CollectPhotoLocalInteractor
import br.com.ecostage.mobilecollect.listener.OnCollectLocalPhotosQueueSizeReadListener
import br.com.ecostage.mobilecollect.listener.OnCollectLocalPhotosSyncListener
import br.com.ecostage.mobilecollect.listener.OnUserLoadedWithoutScoreListener
import br.com.ecostage.mobilecollect.listener.OnUserScoresLoadedListener
import br.com.ecostage.mobilecollect.model.Team
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import br.com.ecostage.mobilecollect.ui.map.MapInteractor
import br.com.ecostage.mobilecollect.ui.map.MapInteractorImpl
import com.google.firebase.storage.UploadTask

/**
 * Created by andremaia on 8/2/17.
 */
class ProfilePresenterImpl(var view: ProfileView) :
        ProfilePresenter,
        ProfileInteractor.OnPasswordResetResult,
        ProfileInteractor.OnLoadTotalCollectsFromUser,
        CollectInteractor.OnTeamListListener,
        ProfileInteractor.OnUserSignOutListener,
        OnMapDownloadListener,
        OnUserLoadedWithoutScoreListener,
        OnUserScoresLoadedListener,
        OnCollectLocalPhotosQueueSizeReadListener,
        OnCollectLocalPhotosSyncListener {

    private val profileInteractor: ProfileInteractor = ProfileInteractorImpl(
            this,
            this,
            this,
            this,
            this,
            this)
    private val mapInteractor: MapInteractor = MapInteractorImpl()
    private val collectPhotoLocalInteractor = CollectPhotoLocalInteractor(
            this,
            this)

    override fun onMapDownloadSuccess() {
        view.showMapDownloadSuccess()
        view.hideProgress()
        view.showMenuBar()
    }

    override fun onMapDownloadFailure() {
        view.showMapDownloadFailure()
        view.hideProgress()
        view.showMenuBar()
    }

    override fun downloadOfflineArea() {
        view.showMapDownloadProgress()
        view.hideMenuBar()
        view.disableScreenTimeout()
        mapInteractor.downloadOfflineArea(this)
    }

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
        profileInteractor.loadTotalScoresFromUser()
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

    override fun onRankingLoaded(userPoints: Int?) {
        view.setUserScore(userPoints)
    }

    override fun onRankingLoadingError() {
        view.setUserScoreOnError()
    }

    override fun loadDataToSync() {
        collectPhotoLocalInteractor.loadTotalPhotosNotSynced()
    }

    override fun onCollectLocalPhotosQueueSizeRead(size: Int) {
        view.setSyncStatus(size)
    }

    override fun syncCollectedData() {
        collectPhotoLocalInteractor.syncCollectedData()
    }

    override fun onCollectLocalPhotosSyncStarted() {
        view.collectSyncStarted()
    }

    override fun onCollectLocalPhotosSynced(result: UploadTask.TaskSnapshot?) {
        when(result?.error) {
            null -> view.showSuccessMessageSyncedData()
            else -> view.showFailMessageSyncedData()
        }
    }

    override fun onCollectLocalPhotosSyncError() {
        view.showFailMessageSyncedData()
    }
    override fun onPermissionDenied(message: String) = view.showMessageAsLongToast(message)

    override fun onPermissionNeeded() = view.showRequestPermissionsDialog()

    override fun onMapDownloadProgress(progress: Float) {
        view.updateMapDownloadProgress(progress)
    }
}