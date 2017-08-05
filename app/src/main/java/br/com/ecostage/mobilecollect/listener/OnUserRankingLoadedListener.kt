package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.Rank

/**
 * Created by cmaia on 8/5/17.
 */
interface OnUserRankingLoadedListener {
    fun onUserRankingLoadedListener(rank: Rank)
    fun onUserRankingLoadedListener() {}
}