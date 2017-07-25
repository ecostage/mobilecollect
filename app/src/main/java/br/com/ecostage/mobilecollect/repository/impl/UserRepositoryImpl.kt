package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by cmaia on 7/23/17.
 */
class UserRepositoryImpl : UserRepository {
    override fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}