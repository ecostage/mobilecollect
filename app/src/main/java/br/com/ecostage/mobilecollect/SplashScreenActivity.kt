package br.com.ecostage.mobilecollect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import br.com.ecostage.mobilecollect.ui.login.LoginActivity
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.fabric.sdk.android.Fabric;



/**
 * Created by andremaia on 7/15/17.
 */
class SplashScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash_screen)

        logUser()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        val timerThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        timerThread.start()
    }

    private fun logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        val instance = FirebaseAuth.getInstance()
        val currentUser = instance.currentUser

        currentUser.let {
            Crashlytics.setUserIdentifier(currentUser?.uid)
            Crashlytics.setUserEmail(currentUser?.email)
            Crashlytics.setUserName(currentUser?.displayName)
        }

    }

}