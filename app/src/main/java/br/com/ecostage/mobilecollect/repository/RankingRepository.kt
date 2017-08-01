package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.OnUserPointsLoadedListener

/**
 * Created by cmaia on 7/30/17.
 */
interface RankingRepository {
    fun getUserPoints(userId: String, onUserPointsLoadedListener: OnUserPointsLoadedListener)
}