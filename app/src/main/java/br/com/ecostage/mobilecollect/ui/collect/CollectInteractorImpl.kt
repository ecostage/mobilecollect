package br.com.ecostage.mobilecollect.ui.collect

import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.TeamRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.TeamRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * Created by cmaia on 7/20/17.
 */
class CollectInteractorImpl(val onSaveCollectListener: CollectInteractor.OnSaveCollectListener,
                            val onCollectLoadedListener: OnCollectLoadedListener,
                            val onTeamListListener: CollectInteractor.OnTeamListListener)
    : CollectInteractor, AnkoLogger {

    val collectRepository: CollectRepository = CollectRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()
    var teamRepository: TeamRepository = TeamRepositoryImpl()

    override fun save(collect: Collect, photoBytes: ByteArray) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            collectRepository.save(userId, collect, photoBytes, onSaveCollectListener)
        } else {
            onSaveCollectListener.onSaveCollectError()
            error { "Could not find current user id when saving collect" }
        }
    }

    override fun loadCollect(collectId: String) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            collectRepository.loadCollect(collectId, userId, onCollectLoadedListener)
        } else {
            error { "Could not find current user id when load collects" }
        }

    }

    override fun loadTeamsListForCurrentUser() {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            teamRepository.loadTeamsFor(userId, onTeamListListener)
        }
        else {
            onTeamListListener.onTeamListError()
            error { "Could not find current user id when loading teams" }
        }
    }

    override fun generateCollectId(): String? {
        return collectRepository.generateCollectId()
    }
}