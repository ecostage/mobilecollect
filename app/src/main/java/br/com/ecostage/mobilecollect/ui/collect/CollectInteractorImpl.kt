package br.com.ecostage.mobilecollect.ui.collect

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * Created by cmaia on 7/20/17.
 */
class CollectInteractorImpl(val onSaveCollectListener: CollectInteractor.OnSaveCollectListener,
                            val onCollectLoadedListener: OnCollectLoadedListener)
    : CollectInteractor, AnkoLogger {

    val collectRepository: CollectRepository = CollectRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()

    override fun save(collect: Collect, photoBytes: ByteArray) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            collectRepository.save(userId, collect, photoBytes, onSaveCollectListener)
        } else {
            error { "Error loading collect" }
            onSaveCollectListener.onSaveCollectError()
        }
    }

    override fun loadCollect(collectId: String) {
        collectRepository.loadCollect(collectId, onCollectLoadedListener)
    }
}