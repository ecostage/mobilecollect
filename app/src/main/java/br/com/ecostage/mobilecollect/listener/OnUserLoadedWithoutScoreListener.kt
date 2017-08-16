package br.com.ecostage.mobilecollect.listener

import br.com.ecostage.mobilecollect.model.User

/**
 * Created by anmaia on 8/4/17.
 */
interface OnUserLoadedWithoutScoreListener {
    fun onUserLoaded(user: User)
    fun onUserLoadingError()
}