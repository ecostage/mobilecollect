package br.com.ecostage.mobilecollect.repository

/**
 * Created by cmaia on 7/23/17.
 */
interface UserRepository {
    fun getCurrentUserId() : String?
}