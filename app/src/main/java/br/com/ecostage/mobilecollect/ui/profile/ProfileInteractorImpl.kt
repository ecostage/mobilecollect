package br.com.ecostage.mobilecollect.ui.profile

import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by andremaia on 8/2/17.
 */
class ProfileInteractorImpl(val onPasswordResetResult: ProfileInteractor.OnPasswordResetResult,
                            val onLoadTotalCollectsFromUser: ProfileInteractor.OnLoadTotalCollectsFromUser)
    : ProfileInteractor {

    val collectRepository: CollectRepository = CollectRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()

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
}