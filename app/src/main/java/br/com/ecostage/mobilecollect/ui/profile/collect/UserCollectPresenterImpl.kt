package br.com.ecostage.mobilecollect.ui.profile.collect

/**
 * Created by cmaia on 7/23/17.
 */
class UserCollectPresenterImpl(userCollectView: UserCollectView,
                               userCollectListAdapter: CollectListAdapter,
                               userCollectActivity: UserCollectActivity)
    : UserCollectPresenter {

    private val userCollectInteractor: UserCollectInteractor = UserCollectInteractorImpl(userCollectListAdapter)

    override fun loadCollects() {
        userCollectInteractor.loadUserCollects()
    }

}