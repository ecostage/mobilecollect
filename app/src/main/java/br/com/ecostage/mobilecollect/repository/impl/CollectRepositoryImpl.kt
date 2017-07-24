package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.ui.collect.Collect
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractorImpl
import com.google.firebase.database.*

/**
 * Created by cmaia on 7/23/17.
 */
class CollectRepositoryImpl : CollectRepository {
    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun loadCollectsByUser(userId: String, onCollectLoadedListener: OnCollectLoadedListener) {
        firebaseDatabase
                .child(CollectInteractorImpl.COLLECT_BY_USER_DB_TYPE)
                .child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        dataSnapshot?.children?.mapNotNull {
                            it.getValue(Collect::class.java)
                        }?.forEach {
                            onCollectLoadedListener.onCollectLoaded(it)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        error { "Error when loading collect data" }
                        onCollectLoadedListener.onCollectLoadedError()
                    }
                })
    }
}