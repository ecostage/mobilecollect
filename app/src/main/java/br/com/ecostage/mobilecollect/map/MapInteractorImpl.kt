package br.com.ecostage.mobilecollect.map

import br.com.ecostage.mobilecollect.collect.Collect
import br.com.ecostage.mobilecollect.collect.CollectInteractorImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.error

/**
 * Created by cmaia on 7/22/17.
 */
class MapInteractorImpl(val collectLoadedListener: MapInteractor.OnCollectLoadedListener,
                        val mapActivity: MapActivity)
    : MapInteractor {

    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun loadUserCollects() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            firebaseDatabase
                    .child(CollectInteractorImpl.COLLECT_BY_USER_DB_TYPE)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            val collect : Collect? = dataSnapshot?.getValue(Collect::class.java)

                            if (collect != null)
                                collectLoadedListener.onCollectLoaded(collect)
                        }

                        override fun onCancelled(databaseError: DatabaseError?) {
                            mapActivity.error("Error when loading collect data")
                            collectLoadedListener.onCollectLoadedError()
                        }
                    })
        }
    }
}