package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor

/**
 * Representation for Team data repository.
 *
 * Created by andremaia on 7/26/17.
 */
interface TeamRepository {

    fun loadTeamsFor(userId: String, onTeamListListener: CollectInteractor.OnTeamListListener)
}