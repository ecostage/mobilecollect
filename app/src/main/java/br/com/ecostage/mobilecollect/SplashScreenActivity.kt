package br.com.ecostage.mobilecollect

import android.app.Activity
import android.os.Bundle
import br.com.ecostage.mobilecollect.ui.login.LoginActivity
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import io.fabric.sdk.android.Fabric
import org.jetbrains.anko.startActivity


/**
 * Created by andremaia on 7/15/17.
 */
class SplashScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        logUser()
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        startActivity<LoginActivity>()
        finish()
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