package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.mapper.CollectMapper
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import br.com.ecostage.mobilecollect.ui.profile.ProfileInteractor
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger

/**
 * Created by cmaia on 7/23/17.
 */
class CollectRepositoryImpl : CollectRepository, AnkoLogger {

    companion object {
        private val COLLECT_DB_TYPE = "collect"
        private val COLLECT_BY_USER_DB_TYPE = "collect_by_user"
        private val COLLECT_RANKING_DB_TYPE = "ranking_collect_by_user"
        private val STORAGE_BUCKET_URL = "gs://mobilecollect-2b822.appspot.com"
        private val STORAGE_BUCKET_PHOTOS_NAME = "collect_photos"
    }

    val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
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
//                        if (databaseError != null) {
//                            onCollectLoadedListener.onCollectLoadedError()
//                            error("Error when loading collect data. " + databaseError.message)
//                        }
                    }
                })
    }

    override fun save(userId: String, collect: Collect, photoBytes: ByteArray, onCollectSaveListener: CollectInteractor.OnSaveCollectListener) {
        collect.userId = userId

        val uid: String? = firebaseDatabase.child(COLLECT_DB_TYPE).push().key

        // Clean the photo because we don't want to save it to firebase -- Check another way to do this
        collect.photo = null

        // Save in the right db
        firebaseDatabase.child(COLLECT_DB_TYPE).child(uid).setValue(collect)

        // Index by user in a new collection
        firebaseDatabase.child(COLLECT_BY_USER_DB_TYPE).child(collect.userId).child(uid).setValue(collect)

        // Increment user points
        firebaseDatabase.child(COLLECT_RANKING_DB_TYPE).child(collect.userId).child("score").runTransaction(object : Transaction.Handler {
            override fun doTransaction(data: MutableData?): Transaction.Result {
                val points = data?.getValue(Long::class.java)

                if (points == null) {
                    data?.value = 1
                } else {
                    data.value = points + 1
                }

                return Transaction.success(data)
            }

            override fun onComplete(dbError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                if (dbError != null) {
                    undoCollect(collect, false)
                    onCollectSaveListener.onSaveCollectError()
                    error("Could not save user [" + collect.userId + "] ranking")
                }
            }
        })

        // Save the photo in storage
        val storageReference = firebaseStorage.child(uid + ".jpg")
        val uploadTask = storageReference.putBytes(photoBytes)

        val savedCollect = CollectMapper().map(collect, uid)

        uploadTask.addOnSuccessListener {
            onCollectSaveListener.onSaveCollect(savedCollect)
        }

        uploadTask.addOnFailureListener {
            undoCollect(collect, true)
            onCollectSaveListener.onSaveCollectError() // Maybe a message or error code
        }
    }

    private fun undoCollect(collect: Collect, hasPointsIncreased: Boolean) {
        firebaseDatabase.child(COLLECT_DB_TYPE).child(collect.userId).removeValue()
        firebaseDatabase.child(COLLECT_BY_USER_DB_TYPE).child(collect.userId).child(collect.userId).removeValue()

        if (hasPointsIncreased) { // This logic to check if points had increased is wrong, check this later
            firebaseDatabase.child(COLLECT_RANKING_DB_TYPE).child(collect.userId).runTransaction(object : Transaction.Handler {
                override fun doTransaction(data: MutableData?): Transaction.Result {
                    val points = data?.getValue(Long::class.java)

                    if (points != null && points > 0L) {
                        data.value = points - 1
                    }

                    return Transaction.success(data)
                }

                override fun onComplete(dbError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                    if (dbError != null) {
                        error("Could not save undo user [" + collect.userId + "] ranking")
                    }
                }
            })
        }
    }

    override fun loadCollect(collectId: String, onCollectLoadedListener: OnCollectLoadedListener) {
        firebaseDatabase
                .child(COLLECT_DB_TYPE)
                .child(collectId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        val collect = dataSnapshot?.getValue(Collect::class.java)
                        collect?.id = dataSnapshot?.key

                        if (collect != null){
                            onCollectLoadedListener.onCollectLoaded(collect)

                            val storageReference = firebaseStorage.child(collect.id + ".jpg")
                            val downloadTask = storageReference.getBytes(1024 * 1024)

                            downloadTask.addOnSuccessListener {
                                collect.photo = it
                                onCollectLoadedListener.onCollectImageLoaded(collect)
                            }

                            downloadTask.addOnFailureListener {
                                onCollectLoadedListener.onCollectImageLoadedError()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
//                        if (databaseError != null) {
//                            onCollectLoadedListener.onCollectLoadedError()
//                            error { "Error when loading collect data. " + databaseError.message }
//                        }
                    }
                })
    }


    override fun countCollectsByUser(userId: String, listener: ProfileInteractor.OnLoadTotalCollectsFromUser) {
        firebaseDatabase
                .child(COLLECT_BY_USER_DB_TYPE)
                .child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(databaseError: DatabaseError?) {
                        listener.onLoadTotalCollectsFromUser(0)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        if (dataSnapshot != null) {
                            listener.onLoadTotalCollectsFromUser(dataSnapshot.childrenCount)
                        } else {
                            listener.onLoadTotalCollectsFromUser(0)
                        }

                    }
                })
    }
}