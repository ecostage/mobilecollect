package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnGeneralRankingLoadedListener
import br.com.ecostage.mobilecollect.listener.OnTeamRankingLoadedListener
import br.com.ecostage.mobilecollect.model.Team
import br.com.ecostage.mobilecollect.model.TeamRanking
import br.com.ecostage.mobilecollect.model.UserRank
import br.com.ecostage.mobilecollect.repository.CollectRepository
import br.com.ecostage.mobilecollect.repository.RankingRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import br.com.ecostage.mobilecollect.repository.impl.CollectRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.RankingRepositoryImpl
import br.com.ecostage.mobilecollect.repository.impl.UserRepositoryImpl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * Created by cmaia on 7/30/17.
 */
class RankingInteractorImpl : RankingInteractor, AnkoLogger {
    val userRepository : UserRepository = UserRepositoryImpl()

    val collectRepository : CollectRepository = CollectRepositoryImpl()
    val rankingRepository : RankingRepository = RankingRepositoryImpl()

    override fun findGeneralRanking(onGeneralRankingLoadedListener: OnGeneralRankingLoadedListener) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            // load

            val user1 = UserRank("ABC", "caaiomaia@gmail.com", 10)
            val user2 = UserRank("XYZ", "andrenmaia@gmail.com", 9)

            val rank = mutableListOf(user1, user2)

            onGeneralRankingLoadedListener.onGeneralRankingLoaded(rank)
        } else {
            onGeneralRankingLoadedListener.onGeneralRankingLoadError()
            error { "Could not find current user id when loading user teams rank" }
        }
    }

    override fun findTeamsRanking(onTeamRankingLoadedListener: OnTeamRankingLoadedListener) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            // load
            val team = Team()
            team.id = "123"
            team.name = "Time 1"

            val user1 = UserRank("ABC", "caaiomaia@gmail.com", 10)
            val user2 = UserRank("XYZ", "andrenmaia@gmail.com", 9)

            val rank1 = TeamRanking(team, mutableListOf(user1, user2))

            val team2 = Team()
            team2.id = "456"
            team2.name = "Time 2"

            val rank2 = TeamRanking(team2, mutableListOf(user1, user2))

            val teamRanking = mutableListOf(rank1, rank2)

            onTeamRankingLoadedListener.onTeamRankingLoaded(teamRanking)
        } else {
            onTeamRankingLoadedListener.onTeamRankingLoadedError()
            error { "Could not find current user id when loading user teams rank" }
        }
    }
}