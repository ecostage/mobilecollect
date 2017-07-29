package br.com.ecostage.mobilecollect.ui.collect

import br.com.ecostage.mobilecollect.ui.model.Team

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectView {
    fun showCamera()
    fun showRequestPermissionsDialog()
    fun canAccessCamera() : Boolean
    fun showMessageAsLongToast(message: String)
    fun showProgress()
    fun showProgressBarForTeams()
    fun hideProgress()
    fun hideProgressBarForTeams()
    fun showCollectRequestSuccess()
    fun returnToMap(collectViewModel: CollectViewModel?)
    fun showNoUserError()
    fun populateFields(collectViewModel: CollectViewModel)
    fun showTeamList(teamsList: Array<Team>)
    fun removeTeamSelected()
    fun showUserHasNoTeamsMessage()
}