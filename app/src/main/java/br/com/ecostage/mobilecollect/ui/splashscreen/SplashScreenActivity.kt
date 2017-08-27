package br.com.ecostage.mobilecollect.ui.splashscreen

import android.app.Activity
import android.os.Bundle
import br.com.ecostage.mobilecollect.ui.login.LoginActivity
import org.jetbrains.anko.startActivity


/**
 * Activity handler for Splash Screen.
 * Created by andremaia on 7/15/17.
 */
class SplashScreenActivity :
        Activity(),
        SplashScreenView {


    private val presenter: SplashScreenPresenter = SplashScreenPresenterImpl(
            this
            , this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.initializeFirebase()
        presenter.setupCrashReports()
    }

    override fun goToNextView() {
        startActivity<LoginActivity>()
        finish()
    }


}