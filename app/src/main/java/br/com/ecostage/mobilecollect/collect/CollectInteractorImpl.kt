package br.com.ecostage.mobilecollect.collect

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by cmaia on 7/20/17.
 */
class CollectInteractorImpl : CollectInteractor {
    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun save(collect: Collect) {
        val uid : String? = firebaseDatabase.child("collect").push().key

        firebaseDatabase.child("collect").child(uid).setValue(collect)
    }
}