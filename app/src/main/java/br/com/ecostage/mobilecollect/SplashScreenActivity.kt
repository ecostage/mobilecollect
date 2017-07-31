package br.com.ecostage.mobilecollect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import br.com.ecostage.mobilecollect.ui.login.LoginActivity
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;



/**
 * Created by andremaia on 7/15/17.
 */
class SplashScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash_screen)

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

}