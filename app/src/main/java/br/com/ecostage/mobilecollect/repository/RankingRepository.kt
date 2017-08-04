package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.listener.OnUserScoresLoadedListener

/**
 * Created by cmaia on 7/30/17.
 */
interface RankingRepository {
    fun getUserScore(userId: String, onUserScoresLoadedListener: OnUserScoresLoadedListener)
}