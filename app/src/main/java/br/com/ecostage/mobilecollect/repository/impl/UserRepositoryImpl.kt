package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.listener.OnUserLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserLoadedWithoutScoreListener
import br.com.ecostage.mobilecollect.listener.OnUserScoresLoadedListener
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.repository.RankingRepository
import br.com.ecostage.mobilecollect.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by cmaia on 7/23/17.
 */
class UserRepositoryImpl : UserRepository {
    private val rankingRepository: RankingRepository = RankingRepositoryImpl()
    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getUser(userId: String, onUserLoadedListener: OnUserLoadedListener) {
        firebaseDatabase
                .child("users")
                .child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(data: DataSnapshot?) {
                        onUserLoadedListener.onUserLoaded(User(userId, "", 0))
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
    }

    override fun getCurrentUserWithoutScore(onUserLoadedWithoutScoreListener: OnUserLoadedWithoutScoreListener) {

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null || user.email == null) {
            onUserLoadedWithoutScoreListener.onUserLoadingError()
            return
        }

        val email = user.email
        if (email !is String) {
            onUserLoadedWithoutScoreListener.onUserLoadingError()
            return
        }

        onUserLoadedWithoutScoreListener.onUserLoaded(User(user.uid, email, -1))

    }

    override fun getCurrentUser(onUserLoadedListener: OnUserLoadedListener) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            rankingRepository.getUserScore(user.uid, object : OnUserScoresLoadedListener {
                override fun onRankingLoaded(userPoints: Int?) {
                    var points = 0

                    if (userPoints != null)
                        points = userPoints

                    points.let { p ->
                        val email = user.email

                        if (email is String) {
                            onUserLoadedListener.onUserLoaded(User(user.uid, email, p))
                        } else {
                            onUserLoadedListener.onUserLoadingError()
                        }
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


    override fun getCurrentUserScore(onUserScoresLoaded: OnUserScoresLoadedListener) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            rankingRepository.getUserScore(userId, onUserScoresLoaded)
        } else {
            onUserScoresLoaded.onRankingLoadingError()
        }
    }

    override fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}