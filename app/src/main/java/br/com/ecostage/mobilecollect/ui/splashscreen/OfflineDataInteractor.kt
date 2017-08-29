package br.com.ecostage.mobilecollect.ui.splashscreen

import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.RankingRepository
import br.com.ecostage.mobilecollect.repository.TeamRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.RankingRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.TeamRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Offline data interactor to handle basic app data and keep it synced.
 *
 * Created by andremaia on 8/27/17.
 */
class OfflineDataInteractor :
        AnkoLogger {

    val userRepo: UserRepository = UserRepositoryImpl()
    val collectRepo: CollectRepository = CollectRepositoryImpl()
    val teamRepo: TeamRepository = TeamRepositoryImpl()
    val rankRepo: RankingRepository = RankingRepositoryImpl()

    fun keepBasedAppDataSynced() {

        val currentUserId = userRepo.getCurrentUserId()

        if (currentUserId == null) {
            info("User not logged")
            return
        }

        collectRepo.keepCollectsSyncedFor(currentUserId)
        teamRepo.keepTeamsSyncedFor(currentUserId)
        rankRepo.keepScoreSyncedFor(currentUserId)
    }
}