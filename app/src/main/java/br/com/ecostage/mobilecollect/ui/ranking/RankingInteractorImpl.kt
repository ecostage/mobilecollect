package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.listener.OnRankingGeneratedListener
import br.com.ecostage.mobilecollect.listener.OnTeamRankingLoadedListener
import br.com.ecostage.mobilecollect.model.*
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

    override fun generateRanking(onRankingGeneratedListener: OnRankingGeneratedListener) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            // load

            val position1 = Rank(1, User(userId, "caaiomaia@gmail.com", 100), null, 100)
            val team1 = Team()
            team1.id = "123"
            team1.name = "Time 1"
            val position2 = Rank(1, null, team1, 75)
            val team2 = Team()
            team2.id = "456"
            team2.name = "Time 2"
            val position3 = Rank(5, null, team2, 25)

            val rank = mutableListOf(position1, position2, position3)

            onRankingGeneratedListener.onGeneralRankingLoaded(rank)
        } else {
            onRankingGeneratedListener.onGeneralRankingLoadError()
            error { "Could not find current user id when loading user teams rank" }
        }
    }

    override fun findGeneralRanking(onRankingGeneratedListener: OnRankingGeneratedListener) {
        val userId = userRepository.getCurrentUserId()

        if (userId != null) {
            // load

            val user1 = UserRank("ABC", "caaiomaia@gmail.com", 10)
            val user2 = UserRank("XYZ", "andrenmaia@gmail.com", 9)

            val rank = mutableListOf(user1, user2)

//            onRankingGeneratedListener.onGeneralRankingLoaded(rank)
        } else {
            onRankingGeneratedListener.onGeneralRankingLoadError()
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