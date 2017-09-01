package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.listener.OnUserGeneralRankingInfoLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserScoresLoadedListener
import br.com.ecostage.mobilecollect.model.User

/**
 * Ranking repository operations.
 *
 * Created by cmaia on 7/30/17.
 */
interface RankingRepository {
    fun getUserScore(userId: String, onUserScoresLoadedListener: OnUserScoresLoadedListener)
    fun getUserGeneralRankingInfo(user: User, onUserGeneralRankingInfoLoadedListener: OnUserGeneralRankingInfoLoadedListener)
    fun keepScoreSyncedFor(currentUserId: String)
}