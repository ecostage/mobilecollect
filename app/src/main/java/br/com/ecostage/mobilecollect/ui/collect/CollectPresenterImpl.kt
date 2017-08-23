package br.com.ecostage.mobilecollect.ui.collect

import android.graphics.Bitmap
import br.com.ecostage.mobilecollect.listener.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.mapper.CollectMapper
import br.com.ecostage.mobilecollect.model.Collect
import br.com.ecostage.mobilecollect.model.Team
import br.com.ecostage.mobilecollect.util.ImageUtil

/**
 * Created by cmaia on 7/20/17.
 */
class CollectPresenterImpl(val collectView: CollectView,
                           val collectActivity: CollectActivity)
    : CollectPresenter,
        CollectInteractor.OnSaveCollectListener,
        OnCollectLoadedListener,
        CollectInteractor.OnTeamListListener {

    override fun setupCollectMode(mode: CollectView.CollectMode) {
        when(mode) {
            CollectView.CollectMode.VISUALIZING -> collectView.hideImageContainers()
            CollectView.CollectMode.COLLECTING -> collectActivity.showImageContainers()
        }
    }

    private val collectInteractor : CollectInteractor = CollectInteractorImpl(
            this,
            this,
            this)

    override fun loadCollect(collectId: String) {
        collectView.showProgress()
        collectInteractor.loadCollect(collectId)
    }

    override fun onCollectLoaded(collect: Collect) {
        val viewModel = CollectMapper().map(collect)
        collectView.populateFields(viewModel)
        collectView.hideProgress()
    }

    override fun onCollectLoadedError() {
        collectView.showMessageAsLongToast("Failed to show collect.")
    }

    override fun decompressMapSnapshot(compressSnapshot: ByteArray): Bitmap = ImageUtil.decompress(compressSnapshot)

    override fun takePhoto() = collectView.showCamera()

    override fun onPermissionsNeeded() = collectView.showRequestPermissionsDialog()

    override fun onPermissionDenied(message: String) = collectView.showMessageAsLongToast(message)

    override fun save(viewModel: CollectViewModel) {
        collectView.showProgress()

        val collect = CollectMapper().map(viewModel)

        viewModel.photo.let { img ->
            if (img != null)
                collectInteractor.save(collect, img)
        }
    }

    override fun onSaveCollect(collect: Collect) {
        collectView.hideProgress()
        collectView.showCollectRequestSuccess()

        val viewModel = CollectMapper().map(collect)
        collectView.returnToMap(viewModel)
    }

    override fun onSaveCollectError() {
        collectView.hideProgress()
        collectView.showNoUserError()
    }

    override fun selectTeam(model: CollectViewModel) {
        collectView.showProgressBarForTeams()
        collectInteractor.loadTeamsListForCurrentUser()
    }

    override fun onTeamListReady(teams: Array<Team>) {
        val teamViewModels = ArrayList<TeamViewModel>()

        teams.mapNotNull {
            val viewModel = TeamViewModel()
            viewModel.name = it.name
            viewModel.id = it.id

            teamViewModels.add(viewModel)
        }

        collectView.showTeamList(teamViewModels)
        collectView.hideProgressBarForTeams()
    }

    override fun onTeamHasNoTeams() {
        collectView.hideProgressBarForTeams()
        collectView.showUserHasNoTeamsMessage()
    }

    override fun onTeamListError() {
        collectView.hideProgress()
        collectView.showNoUserError()
    }

    override fun removeTeamSelected(model: CollectViewModel) {
        collectView.removeTeamSelected()
    }

    override fun compressCollectPhoto(filePath: String, format: Bitmap.CompressFormat, qualityLevel: Int): ByteArray =
            ImageUtil.compress(filePath, format, qualityLevel)

    override fun convertCollectPhoto(img: ByteArray): Bitmap = ImageUtil.convertToBitmap(img)
}
