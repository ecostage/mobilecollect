package br.com.ecostage.mobilecollect.ui.collect

import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.ui.model.Team

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectInteractor {

    interface OnSaveCollectListener {
        fun onSaveCollect(collect: Collect)
        fun onSaveCollectError()
    }

    interface OnTeamListListener {
        fun onTeamListReady(teams: Array<Team>)
        fun onTeamHasNoTeams()
        fun onTeamListError()
    }

    fun save(collect: Collect, photoBytes: ByteArray)
    fun loadCollect(collectId: String)
    fun loadTeamsListForCurrentUser()
}