package br.com.ecostage.mobilecollect.ui.collect

import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.TeamRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.TeamRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

    val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun save(collect: Collect) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null)
            collectRepository.save(userId, collect, onSaveCollectListener)
        else {
            error { "Error loading collect" }
            onSaveCollectListener.onSaveCollectError()
        }
    }

    override fun loadCollect(collectId: String) {
        collectRepository.loadCollect(collectId, onCollectLoadedListener)
    }

    override fun loadTeamsListForCurrentUser() {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            teamRepository.loadTeamsFor(userId, onTeamListListener)
        }
        else {
            error { "Error loading teams" }
            onTeamListListener.onTeamListError()
        }
    }
}