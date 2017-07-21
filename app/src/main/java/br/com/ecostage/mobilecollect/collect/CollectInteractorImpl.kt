package br.com.ecostage.mobilecollect.collect

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by cmaia on 7/20/17.
 */
class CollectInteractorImpl : CollectInteractor {
    companion object {
        val COLLECT_DB_TYPE = "collect"
        val COLLECT_BY_USER_DB_TYPE = "collect_by_user"
    }
    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun save(collect: Collect) : Collect {
        val uid : String? = firebaseDatabase.child(COLLECT_DB_TYPE).push().key

        // Save in the right db
        firebaseDatabase.child(COLLECT_DB_TYPE).child(uid).setValue(collect)

        // Index by user in a new collection
        firebaseDatabase.child(COLLECT_BY_USER_DB_TYPE).child(collect.userId).child(uid).setValue(collect)

        return Collect(uid, collect.name, collect.latitude, collect.longitude, collect.classification, collect.userId)
    }
}