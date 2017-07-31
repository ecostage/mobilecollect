package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.OnUserLoadedListener
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl

/**
 * Created by cmaia on 7/30/17.
 */
class RankingInteractorImpl(val onUserLoadedListener: OnUserLoadedListener) : RankingInteractor {
    val userRepository : UserRepository = UserRepositoryImpl()

    override fun findUser() {
        userRepository.getCurrentUser(onUserLoadedListener)
    }
}