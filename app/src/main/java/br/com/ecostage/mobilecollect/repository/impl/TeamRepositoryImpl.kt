package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.repository.TeamRepository
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamRepositoryImpl : TeamRepository {

    override fun loadTeamsFor(userId: String, onTeamListListener: CollectInteractor.OnTeamListListener) {

        doAsync {
            val result = arrayOf<CharSequence>("Red", "Green", "Blue", "Red", "Green", "Blue", "Red", "Green", "Blue", "Red", "Green", "Blue", "Red", "Green", "Blue", "Red", "Green", "Blue")
            Thread.sleep(5000)

            uiThread {
                onTeamListListener.onTeamListReady(result)
            }
        }
    }


}