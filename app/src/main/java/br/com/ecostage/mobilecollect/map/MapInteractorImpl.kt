package br.com.ecostage.mobilecollect.map

import br.com.ecostage.mobilecollect.collect.Collect
import br.com.ecostage.mobilecollect.collect.CollectInteractorImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * Created by cmaia on 7/22/17.
 */
class MapInteractorImpl(val collectLoadedListener: MapInteractor.OnCollectLoadedListener,
                        val mapActivity: MapActivity)
    : MapInteractor, AnkoLogger {

    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun loadUserCollects() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            firebaseDatabase
                    .child(CollectInteractorImpl.COLLECT_BY_USER_DB_TYPE)
                    .child(userId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {

                            dataSnapshot?.children?.mapNotNull {
                                it.getValue(Collect::class.java)
                            }?.forEach {
                                collectLoadedListener.onCollectLoaded(it)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError?) {
                            error { "Error when loading collect data" }
                            collectLoadedListener.onCollectLoadedError()
                        }
                    })
        }
    }
}