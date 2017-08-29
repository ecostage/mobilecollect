package br.com.ecostage.mobilecollect.ui.splashscreen

import android.os.Environment
import br.com.ecostage.mobilecollect.interactor.CollectPhotoLocalInteractor
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


    override fun onCreated() {
        this.setupCrashReports()
        this.setupCollectPhotoLocalInteractor()
        view.goToNextView()
    }

    private fun setupCrashReports() {
        Fabric.with(viewContext, Crashlytics())
        logUser()
    }

    private fun setupCollectPhotoLocalInteractor() {
        CollectPhotoLocalInteractor.COLLECT_PHOTO_PATH = viewContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/collect_photos")
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