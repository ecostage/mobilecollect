package br.com.ecostage.mobilecollect

import br.com.ecostage.mobilecollect.model.User

/**
 * Created by cmaia on 7/30/17.
 */
interface OnUserLoadedListener {
    fun onUserLoaded(user: User)
    fun onUserLoadingError()
}