package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.listener.OnUserPointsLoadedListener
import br.com.ecostage.mobilecollect.repository.RankingRepository
import com.google.firebase.database.*

/**
 * Created by cmaia on 7/30/17.
 */
class RankingRepositoryImpl : RankingRepository {
    companion object {
        private val RANKING_COLLECT_BY_USER_DB_TYPE = "ranking_collect_by_user"
    }

    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getUserPoints(userId: String, onUserPointsLoadedListener: OnUserPointsLoadedListener) {
        firebaseDatabase
                .child(RANKING_COLLECT_BY_USER_DB_TYPE)
                .child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(data: DataSnapshot?) {
                        val points = data?.child("score")?.getValue(Int::class.java)
                        if (points != null) {
                            onUserPointsLoadedListener.onRankingLoaded(points)
                        }
                    }

                    override fun onCancelled(dbError: DatabaseError?) {
                        onUserPointsLoadedListener.onRankingLoadingError()
                    }
                })
    }
}