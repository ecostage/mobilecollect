package br.com.ecostage.mobilecollect.ui.splashscreen

import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.fabric.sdk.android.Fabric

/**
 * Default Splash Screen Presenter implementation.
 *
 * Created by andremaia on 8/27/17.
 */
class SplashScreenPresenterImpl(val viewContext: SplashScreenActivity, val view: SplashScreenView) :
        SplashScreenPresenter {


    override fun initializeFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    override fun setupCrashReports() {
        Fabric.with(viewContext, Crashlytics())
        logUser()

        view.goToNextView()
    }

    private fun logUser() {
        val instance = FirebaseAuth.getInstance()
        val currentUser = instance.currentUser

        currentUser.let {
            Crashlytics.setUserIdentifier(currentUser?.uid)
            Crashlytics.setUserEmail(currentUser?.email)
            Crashlytics.setUserName(currentUser?.displayName)
        }

    }
}