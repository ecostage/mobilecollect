package br.com.ecostage.mobilecollect.ui.map

import br.com.ecostage.mobilecollect.listener.OnCollectAvailableLoadedListener
import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import org.jetbrains.anko.AnkoLogger

/**
 * Created by cmaia on 7/22/17.
 */
class MapInteractorImpl(val collectLoadedListener: OnCollectLoadedListener,
                        val collectAvailableLoadedListener: OnCollectAvailableLoadedListener)
    : MapInteractor, AnkoLogger {

    val userRepository: UserRepository = UserRepositoryImpl()
    val collectRepository: CollectRepository = CollectRepositoryImpl()

    override fun loadUserCollects() {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            collectRepository.loadCollectsByUser(userId, collectLoadedListener)
        }
    }

    override fun loadCollectsAvailable() {
        collectRepository.loadCollectsAvailable(collectAvailableLoadedListener)
    }
}