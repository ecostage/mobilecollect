package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.listener.OnUserLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserPointsLoadedListener
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.repository.RankingRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by cmaia on 7/23/17.
 */
class UserRepositoryImpl : UserRepository {
    private val rankingRepository: RankingRepository = RankingRepositoryImpl()

    override fun getCurrentUser(onUserLoadedListener: OnUserLoadedListener) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            rankingRepository.getUserPoints(user.uid, object : OnUserPointsLoadedListener {
                override fun onRankingLoaded(userPoints: Int?) {
                    var points = 0

                    if (userPoints != null)
                        points = userPoints

                    points.let { p ->
                        val email = user.email

                        if (email is String) {
                            onUserLoadedListener.onUserLoaded(User(user.uid, email, p))
                        }
                        else
                            onUserLoadedListener.onUserLoadingError()
                    }
                }

                override fun onRankingLoadingError() {
                    onUserLoadedListener.onUserLoadingError()
                }

            })
        } else {
            onUserLoadedListener.onUserLoadingError()
        }
    }

    override fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}