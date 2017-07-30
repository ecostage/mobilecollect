package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Created by cmaia on 7/23/17.
 */
class CollectRepositoryImpl : CollectRepository {

    companion object {
        val COLLECT_DB_TYPE = "collect"
        val COLLECT_BY_USER_DB_TYPE = "collect_by_user"
        private val STORAGE_BUCKET_URL = "gs://mobilecollect-2b822.appspot.com"
        private val STORAGE_BUCKET_PHOTOS_NAME = "collect_photos"
    }

    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference
    val firebaseStorage: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_BUCKET_URL).child(STORAGE_BUCKET_PHOTOS_NAME)

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
                        onCollectLoadedListener.onCollectLoadedError()
                        error { "Error when loading collect data" }
                    }
                })
    }

    override fun save(userId: String, collect: Collect, photoBytes: ByteArray, onCollectSaveListener: CollectInteractor.OnSaveCollectListener) {
        collect.userId = userId

        val uid : String? = firebaseDatabase.child(COLLECT_DB_TYPE).push().key

        // Clean the photo because we don't want to save it to firebase -- Check another way to do this
        collect.photo = null

        // Save in the right db
        firebaseDatabase.child(COLLECT_DB_TYPE).child(uid).setValue(collect)

        // Index by user in a new collection
        firebaseDatabase.child(COLLECT_BY_USER_DB_TYPE).child(collect.userId).child(uid).setValue(collect)

        // Save the photo in storage
        val storageReference = firebaseStorage.child(uid + ".jpg")
        val uploadTask = storageReference.putBytes(photoBytes)

        val savedCollect = Collect()

        savedCollect.id = uid
        savedCollect.name = collect.name
        savedCollect.latitude = collect.latitude
        savedCollect.longitude = collect.longitude
        savedCollect.classification = collect.classification
        savedCollect.userId = collect.userId
        savedCollect.date = collect.date

        uploadTask.addOnSuccessListener {
//            savedCollect.photo = it.downloadUrl

            onCollectSaveListener.onSaveCollect(savedCollect)
        }

        uploadTask.addOnFailureListener {
            // Undo collect
            firebaseDatabase.child(COLLECT_DB_TYPE).child(uid).removeValue()
            firebaseDatabase.child(COLLECT_BY_USER_DB_TYPE).child(collect.userId).child(uid).removeValue()

            onCollectSaveListener.onSaveCollectError() // Maybe a message or error code
        }

    }

    override fun loadCollect(collectId: String, onCollectLoadedListener: OnCollectLoadedListener) {
        firebaseDatabase
                .child(COLLECT_DB_TYPE)
                .child(collectId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        val collect = dataSnapshot?.getValue(Collect::class.java)
                        collect?.id =  dataSnapshot?.key

                        if (collect != null) {
                            val storageReference = firebaseStorage.child(collect.id + ".jpg")
                            val downloadTask = storageReference.getBytes(1024*1024)

                            downloadTask.addOnSuccessListener {
                                collect.photo = it
                                onCollectLoadedListener.onCollectLoaded(collect)
                            }

                            downloadTask.addOnFailureListener {
                                onCollectLoadedListener.onCollectLoadedError()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        onCollectLoadedListener.onCollectLoadedError()
                        error { "Error when loading collect data" }
                    }
                })
    }
}