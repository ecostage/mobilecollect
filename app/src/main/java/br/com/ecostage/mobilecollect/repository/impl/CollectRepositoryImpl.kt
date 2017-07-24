package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.ui.collect.Collect
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import com.google.firebase.database.*

/**
 * Created by cmaia on 7/23/17.
 */
class CollectRepositoryImpl : CollectRepository {

    companion object {
        val COLLECT_DB_TYPE = "collect"
        val COLLECT_BY_USER_DB_TYPE = "collect_by_user"
    }

    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun loadCollectsByUser(userId: String, onCollectLoadedListener: OnCollectLoadedListener) {
        firebaseDatabase
                .child(COLLECT_BY_USER_DB_TYPE)
                .child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        dataSnapshot?.children?.mapNotNull {
                            val collect = it.getValue(Collect::class.java)
                            collect?.id = it.key
                            collect
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

    override fun save(userId: String, collect: Collect, onCollectSaveListener: CollectInteractor.OnSaveCollectListener) {
        collect.userId = userId

        val uid : String? = firebaseDatabase.child(COLLECT_DB_TYPE).push().key

        // Save in the right db
        firebaseDatabase.child(COLLECT_DB_TYPE).child(uid).setValue(collect)

        // Index by user in a new collection
        firebaseDatabase.child(COLLECT_BY_USER_DB_TYPE).child(collect.userId).child(uid).setValue(collect)

        val savedCollect = Collect()

        savedCollect.id = uid
        savedCollect.name = collect.name
        savedCollect.latitude = collect.latitude
        savedCollect.longitude = collect.longitude
        savedCollect.classification = collect.classification
        savedCollect.userId = collect.userId
        savedCollect.date = collect.date

        onCollectSaveListener.onSaveCollect(savedCollect)
    }

    override fun loadCollect(collectId: String, onCollectLoadedListener: OnCollectLoadedListener) {
        firebaseDatabase
                .child(COLLECT_DB_TYPE)
                .child(collectId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        val collect = dataSnapshot?.getValue(Collect::class.java)
                        collect?.id =  dataSnapshot?.key

                        if (collect != null)
                            onCollectLoadedListener.onCollectLoaded(collect)
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        error { "Error when loading collect data" }
                        onCollectLoadedListener.onCollectLoadedError()
                    }
                })
    }
}