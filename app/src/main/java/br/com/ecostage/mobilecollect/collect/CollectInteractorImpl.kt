package br.com.ecostage.mobilecollect.collect

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * Created by cmaia on 7/20/17.
 */
class CollectInteractorImpl(val onSaveCollectListener: CollectInteractor.OnSaveCollectListener)
    : CollectInteractor, AnkoLogger {
    companion object {
        val COLLECT_DB_TYPE = "collect"
        val COLLECT_BY_USER_DB_TYPE = "collect_by_user"
    }
    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun save(collect: Collect) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
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

            onSaveCollectListener.onSaveCollect(savedCollect)
        } else {
            error { "Error loading collect" }
            onSaveCollectListener.onSaveCollectError()
        }
    }
}