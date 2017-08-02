package br.com.ecostage.mobilecollect.ui.profile

import com.google.firebase.auth.FirebaseAuth

/**
 * Created by andremaia on 8/2/17.
 */
class ProfileInteractorImpl(val onPasswordResetResult: ProfileInteractor.OnPasswordResetResult) : ProfileInteractor {

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

}