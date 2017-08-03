package br.com.ecostage.mobilecollect.ui.profile

/**
 * Created by andremaia on 8/2/17.
 */
interface ProfilePresenter {
    fun resetPasswordRequest()
    fun loadTotalCollectsFromUser()
    fun loadTotalScoresFromUser()
    fun loadTeamsListFromUser()
}