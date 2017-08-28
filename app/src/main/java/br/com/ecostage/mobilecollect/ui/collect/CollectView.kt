package br.com.ecostage.mobilecollect.ui.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectView {
    enum class CollectMode {
        VISUALIZING,
        COLLECTING
    }

    fun showCamera()
    fun showRequestPermissionsDialog()
    fun canAccessCamera() : Boolean
    fun showMessageAsLongToast(message: String)
    fun showProgress()
    fun showProgressBarForTeams()
    fun hideProgress()
    fun hideProgressBarForTeams()
    fun showCollectRequestSuccess()
    fun showCollectRequestRegistered()
    fun returnToMap(collectViewModel: CollectViewModel?)
    fun showNoUserError()
    fun populateFields(collectViewModel: CollectViewModel)
    fun populateCollectImage(collectViewModel: CollectViewModel)
    fun showTeamList(teamsList: ArrayList<TeamViewModel>)
    fun removeTeamSelected()
    fun showUserHasNoTeamsMessage()
    fun hideImageContainers()
    fun showImageContainers()
}
