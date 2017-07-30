package br.com.ecostage.mobilecollect.repository

import br.com.ecostage.mobilecollect.OnUserLoadedListener

/**
 * Created by cmaia on 7/23/17.
 */
interface UserRepository {
    fun getCurrentUserId() : String?
    fun getCurrentUser(onUserLoadedListener: OnUserLoadedListener)
}