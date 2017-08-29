package br.com.ecostage.mobilecollect

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by cmaia on 8/29/17.
 */
class MobileCollectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}