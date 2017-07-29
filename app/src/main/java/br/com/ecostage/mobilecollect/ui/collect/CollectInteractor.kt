package br.com.ecostage.mobilecollect.ui.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectInteractor {

    interface OnSaveCollectListener {
        fun onSaveCollect(collect: Collect)
        fun onSaveCollectError()
    }

    interface OnTeamListListener {
        fun onTeamListReady(teams: Array<CharSequence>)
        fun onTeamHasNoTeams()
        fun onTeamListError()
    }

    fun save(collect: Collect)
    fun loadCollect(collectId: String)
    fun loadTeamsListForCurrentUser()
}