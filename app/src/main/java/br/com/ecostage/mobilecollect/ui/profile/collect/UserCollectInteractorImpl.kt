package br.com.ecostage.mobilecollect.ui.profile.collect

import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl

/**
 * Created by cmaia on 7/23/17.
 */
class UserCollectInteractorImpl(val collectLoadedListener: OnCollectLoadedListener)
    : UserCollectInteractor {

    val userRepository: UserRepository = UserRepositoryImpl()
    val collectRepository: CollectRepository = CollectRepositoryImpl()

    override fun loadUserCollects() {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            collectRepository.loadCollectsByUser(userId, collectLoadedListener)
        }
    }
}