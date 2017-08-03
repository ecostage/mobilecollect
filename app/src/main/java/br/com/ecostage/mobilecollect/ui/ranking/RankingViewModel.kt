package br.com.ecostage.mobilecollect.ui.ranking

import br.com.ecostage.mobilecollect.ui.collect.TeamViewModel
import br.com.ecostage.mobilecollect.ui.profile.UserViewModel

/**
 * Created by cmaia on 8/2/17.
 */
data class RankingViewModel(val position : Int, val user: UserViewModel?, val team: TeamViewModel?, val points: Int)