package br.com.ecostage.mobilecollect.profile.collect

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.collect.Collect
import br.com.ecostage.mobilecollect.collect.CollectInteractorImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by cmaia on 7/23/17.
 */
class UserCollectInteractorImpl(val collectLoadedListener: OnCollectLoadedListener)
    : UserCollectInteractor {

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