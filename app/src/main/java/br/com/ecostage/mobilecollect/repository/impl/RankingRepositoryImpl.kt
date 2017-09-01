package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.listener.OnUserGeneralRankingInfoLoadedListener
import br.com.ecostage.mobilecollect.listener.OnUserScoresLoadedListener
import br.com.ecostage.mobilecollect.model.Rank
import br.com.ecostage.mobilecollect.model.User
import br.com.ecostage.mobilecollect.repository.RankingRepository
import com.google.firebase.database.*

/**
 * Default ranking repository operations implementation.
 * Created by cmaia on 7/30/17.
 */
class RankingRepositoryImpl : RankingRepository {
    companion object {
        private val RANKING_COLLECT_BY_USER_DB_TYPE = "ranking_collect_by_user"
        private val SORTED_BY_POSITION_RANKING_DB_TYPE = "sorted_by_position_ranking"
        private val USER_RANKING_POSITION_DB_TYPE = "user_ranking_position"
    }

    val firebaseDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getUserScore(userId: String, onUserScoresLoadedListener: OnUserScoresLoadedListener) {
        firebaseDatabase
                .child(RANKING_COLLECT_BY_USER_DB_TYPE)
                .child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(data: DataSnapshot?) {
                        val points = data?.child("score")?.getValue(Int::class.java)
                        if (points != null) {
                            onUserScoresLoadedListener.onRankingLoaded(points)
                        } else {
                            onUserScoresLoadedListener.onRankingLoadingError()
                        }
                    }

                    override fun onCancelled(dbError: DatabaseError?) {
                        if (dbError != null) {
                            onUserScoresLoadedListener.onRankingLoadingError()
                        }
                    }
                })
    }

    override fun getUserGeneralRankingInfo(user: User, onUserGeneralRankingInfoLoadedListener: OnUserGeneralRankingInfoLoadedListener) {
        firebaseDatabase
                .child(USER_RANKING_POSITION_DB_TYPE)
                .child(user.id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(data: DataSnapshot?) {
                        if (data != null && data.value != null) {

                            val position = data.value.toString()

                            firebaseDatabase
                                    .child(SORTED_BY_POSITION_RANKING_DB_TYPE)
                                    .orderByKey()
                                    .startAt((position.toInt() - 3).toString())
                                    .endAt((position.toInt() + 3).toString())
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(sortedData: DataSnapshot?) {
                                            val result = ArrayList<Rank>()

                                            sortedData?.children?.mapNotNull {
                                                val score = it.child("score").getValue(Int::class.java)
                                                val userId = it.child("userId").getValue(String::class.java)
                                                val userEmail = it.child("userEmail").getValue(String::class.java)

                                                if (userId != null && score != null && userEmail != null) {
                                                    val rank = Rank(it.key.toInt(), User(userId, userEmail, score), null, score)
                                                    result.add(rank)
                                                }

                                            }

                                            onUserGeneralRankingInfoLoadedListener.onUserGeneralRankingInfoLoaded(result)
                                        }

                                        override fun onCancelled(p0: DatabaseError?) {
                                            onUserGeneralRankingInfoLoadedListener.onUserGeneralRankingInfoLoadError()
                                        }

                                    })
                        } else {
                            onUserGeneralRankingInfoLoadedListener.onUserGeneralRankingInfoLoadError()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        onUserGeneralRankingInfoLoadedListener.onUserGeneralRankingInfoLoadError()
                    }

                })
    }

    override fun keepScoreSyncedFor(currentUserId: String) {

        firebaseDatabase
                .child(RANKING_COLLECT_BY_USER_DB_TYPE)
                .child(currentUserId)
                .keepSynced(true)

        firebaseDatabase
                .child(SORTED_BY_POSITION_RANKING_DB_TYPE)
                .keepSynced(true)

        firebaseDatabase
                .child(USER_RANKING_POSITION_DB_TYPE)
                .child(currentUserId)
                .keepSynced(true)
    }

}