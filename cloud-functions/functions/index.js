'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sortRankOnWrite = functions.database.ref('/ranking_collect_by_user/{pushId}').onWrite(event => calculate_scoreboard(event));

function calculate_scoreboard(event) {

	if (!event.data.exists()) {
		// The data is being deleted
		console.log("removing " + event.params.pushId + " from sorted rank");

		admin.database().ref('/user_ranking_position/' + event.params.pushId).remove();
	}

	admin.database().ref('/ranking_collect_by_user').orderByChild('score').once('value')
		.then(rankingRef => {
			var entities = [];
			var processedEntities = new Map();
			var count = 0;

			rankingRef.forEach(function(child) {
				entities[count] = child;
				count++;
			});

			entities.reverse(); // Reverse because it comes in ascend order
			
			var position;			
			for (var i = 0; i < entities.length; i++) {
				console.log("entity: " + entities[i].getKey());

				position = i + 1;

				var userScore = entities[i].val().score;
				var userId = entities[i].getKey();

				processedEntities.set(userId,  { score: userScore, position: position });

				admin.auth().getUser(entities[i].getKey())
					.then(function (user) {
						var userRankInfo = processedEntities.get(user.uid);

						if (userRankInfo != null) {
							admin.database().ref('/sorted_by_position_ranking/' + userRankInfo.position).set({
								score: userRankInfo.score,
								userId: user.uid,
								userEmail: user.email
							});

							admin.database().ref('/user_ranking_position/' + user.uid).set(userRankInfo.position);
						}
					})
					.catch(err => {
						console.log(err.stack);
					});

			}

			if (!event.data.exists()) {
				admin.database().ref("/sorted_by_position_ranking/")
					.orderByKey()
					.startAt((position + 1).toString())
					.once('value')
					.then(ref => {
						ref.forEach(function (child) {
							admin.database().ref("/sorted_by_position_ranking/" + child.getKey()).remove();
						});
					});
			}

			return;
		})
		.catch(err => {
			console.log(err.stack);
			return;
		});
}