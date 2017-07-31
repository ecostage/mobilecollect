package br.com.ecostage.mobilecollect.repository.impl

import br.com.ecostage.mobilecollect.model.Team
import br.com.ecostage.mobilecollect.repository.TeamRepository
import br.com.ecostage.mobilecollect.ui.collect.CollectInteractor
import com.google.firebase.database.*


class TeamRepositoryImpl : TeamRepository {

    private val USER_TEAMS_COLLECTION = "team_by_user"

    val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun loadTeamsFor(userId: String, onTeamListListener: CollectInteractor.OnTeamListListener) {

        firebaseDatabase
                .child(USER_TEAMS_COLLECTION)
                .child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        if (dataSnapshot == null) {
                            onTeamListListener.onTeamListError()
                            return
                        }

                        if (dataSnapshot.childrenCount <= 0 ){
                            onTeamListListener.onTeamHasNoTeams()
                            return
                        }

                        val arr = ArrayList<Team>()
                        val children = dataSnapshot.children

                        for (teamSnapshot in children) {
                            val team = teamSnapshot.getValue(Team::class.java)
                            team?.id = teamSnapshot?.key
                            arr.add(team!!)
                        }


                        onTeamListListener.onTeamListReady(arr.toTypedArray())
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        onTeamListListener.onTeamListError()
                        error { "Error when loading team data" }

                    }
                })

    }
}