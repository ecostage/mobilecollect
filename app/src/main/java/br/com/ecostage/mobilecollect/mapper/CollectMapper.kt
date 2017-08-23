package br.com.ecostage.mobilecollect.mapper

import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.model.Team
import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel
import br.com.ecostage.mobilecollect.ui.collect.TeamViewModel

@Suppress("UNUSED_EXPRESSION")
/**
 * Mappers between collect objects
 * Created by andremaia on 8/23/17.
 */
class CollectMapper {

    fun map(viewModel: CollectViewModel): Collect {
        val collect = Collect()

        collect.name = viewModel.name
        collect.latitude = viewModel.latitude
        collect.longitude = viewModel.longitude
        collect.date = viewModel.date
        collect.classification = viewModel.classification
        collect.team = mapTeam(viewModel)
        collect.comments = viewModel.comments

        return collect
    }

    private fun mapTeam(viewModel: CollectViewModel): Team {
        val team = Team()
        team.id = viewModel.team?.id
        team.name = viewModel.team?.name

        return team
    }


    fun map(model: Collect): CollectViewModel {
        val viewModel = CollectViewModel()

        viewModel.id = model.id
        viewModel.name = model.name
        viewModel.latitude = model.latitude
        viewModel.longitude = model.longitude
        viewModel.classification = model.classification
        viewModel.userId = model.userId
        viewModel.date = model.date
        viewModel.photo = model.photo
        viewModel.team = mapTeam(model)
        viewModel.comments = model.comments

        return viewModel
    }

    private fun mapTeam(model: Collect): TeamViewModel {
        val teamViewModel = TeamViewModel()
        teamViewModel.name = model.team?.name
        teamViewModel.id = model.team?.id

        return teamViewModel
    }

    fun map(model: Collect, id: String?): Collect {
        val copy = Collect()

        copy.id = id
        copy.name = model.name
        copy.latitude = model.latitude
        copy.longitude = model.longitude
        copy.classification = model.classification
        copy.userId = model.userId
        copy.date = model.date
        copy.comments = model.comments

        return copy
    }
}