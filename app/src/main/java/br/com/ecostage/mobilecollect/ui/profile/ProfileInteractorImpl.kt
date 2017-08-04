package br.com.ecostage.mobilecollect.ui.profile

import br.com.ecostage.mobilecollect.listener.OnUserLoadedWithoutScoreListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.TeamRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.TeamRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by andremaia on 8/2/17.
 */
class ProfileInteractorImpl(val onPasswordResetResult: ProfileInteractor.OnPasswordResetResult,
                            val onLoadTotalCollectsFromUser: ProfileInteractor.OnLoadTotalCollectsFromUser,
                            // FIXME: Listeners should not belongs to um interactor.
                            val onTeamListListener: CollectInteractor.OnTeamListListener,
                            val onUserSignOutListener: ProfileInteractor.OnUserSignOutListener,
                            val onUserLoadedWithoutScoreListener: OnUserLoadedWithoutScoreListener)
    : ProfileInteractor {

    val collectRepository: CollectRepository = CollectRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()
    val teamRepository: TeamRepository = TeamRepositoryImpl()

    override fun requestResetPasswordToFirebase() {
        val auth = FirebaseAuth.getInstance()
        val emailAddress = auth.currentUser?.email

        // TODO: check if the user is using just google sign on here
        if (emailAddress == null) {
            onPasswordResetResult.onPasswordResetUserIsNotUsingEmailAuth()
            return
        }

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onPasswordResetResult.onPasswordResetSuccess(emailAddress)
                    } else {
                        onPasswordResetResult.onPasswordResetError(emailAddress)
                    }
                }
    }


    override fun loadTotalCollectsFromUser() {

        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            collectRepository.countCollectsByUser(userId, onLoadTotalCollectsFromUser)
        }
    }

    override fun loadTeamsListFromUser() {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            teamRepository.loadTeamsFor(userId, onTeamListListener)
        }
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
        onUserSignOutListener.onUserSignOut()
    }

    override fun loadCurrentUser() {
        userRepository.getCurrentUserWithoutScore(onUserLoadedWithoutScoreListener)
    }
}