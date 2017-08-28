package br.com.ecostage.mobilecollect.ui.map

import android.os.Environment
import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.listener.OnMapDownloadListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.File

/**
 * Created by cmaia on 7/22/17.
 */
class MapInteractorImpl(val collectLoadedListener: OnCollectLoadedListener? = null)
    : MapInteractor, AnkoLogger {

    companion object {
        private val STORAGE_BUCKET_URL = "gs://mobilecollect-2b822.appspot.com"
        private val STORAGE_BUCKET_MAP_TILES = "map_tiles"
    }

    val userRepository: UserRepository = UserRepositoryImpl()
    val collectRepository: CollectRepository = CollectRepositoryImpl()
    val firebaseStorage: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_BUCKET_URL).child(STORAGE_BUCKET_MAP_TILES)

    override fun loadUserCollects() {
        val userId = userRepository.getCurrentUserId()

        if (userId != null && collectLoadedListener != null) {
            collectRepository.loadCollectsByUser(userId, collectLoadedListener)
        }
    }

    override fun downloadOfflineArea(onMapDownloadListener: OnMapDownloadListener) {
        val storageReference = firebaseStorage.child("mobilecollect-1.0.1.mbtiles")
        val localFile = File(Environment.getExternalStorageDirectory().absolutePath, "mobilecollect.mbtiles")

        storageReference.getFile(localFile).addOnSuccessListener {
            onMapDownloadListener.onMapDownloadSuccess()
        }.addOnFailureListener {
            val file = File(Environment.getExternalStorageDirectory().absolutePath + "/mobilecollect-1.0.1.mbtiles")
            if(file.exists()) {
                file.canonicalFile.delete()
            }

            onMapDownloadListener.onMapDownloadFailure()
        }
    }
}