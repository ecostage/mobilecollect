'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sortRankOnWrite = functions.database.ref('/ranking_collect_by_user/{pushId}').onWrite(event => calculate_scoreboard(event));
exports.createInitialRankWhenRegister = functions.auth.user().onCreate(event => create_initial_rank(event));

function create_initial_rank(event) {
	return admin.database().ref('/ranking_collect_by_user/' + event.data.uid).set({
		score: 0
	});
}

function calculate_scoreboard(event) {
	var promises = [];

	if (!event.data.exists()) {
		// The data is being deleted
		console.log("removing " + event.params.pushId + " from sorted rank");

		promises.push(admin.database().ref('/user_ranking_position/' + event.params.pushId).remove());
	}

	promises.push(admin.database().ref('/ranking_collect_by_user').orderByChild('score').once('value')
		.then(rankingRef => {
			var entities = [];
			var processedEntities = new Map();
			var count = 0;
			var innerPromises = [];

			rankingRef.forEach(function(child) {
				entities[count] = child;
				count++;
			});

			entities.reverse(); // Reverse because it comes in ascend order
			
			var position;
			for (var i = 0; i < entities.length; i++) {
				position = i + 1;

				var userScore = entities[i].val().score;
				var userId = entities[i].getKey();

				processedEntities.set(userId,  { score: userScore, position: position });

				var updatePromise = admin.auth().getUser(entities[i].getKey())
					.then(function (user) {
						var userRankInfo = processedEntities.get(user.uid);

						if (userRankInfo != null) {
							var sortPromise = admin.database().ref('/sorted_by_position_ranking/' + userRankInfo.position).set({
								score: userRankInfo.score,
								userId: user.uid,
								userEmail: user.email
							});

							var positionPromise = admin.database().ref('/user_ranking_position/' + user.uid).set(userRankInfo.position);

							return Promise.all([sortPromise, positionPromise]);
						}
					});

				innerPromises.push(updatePromise);
			}

			if (!event.data.exists()) {
				innerPromises.push(admin.database().ref("/sorted_by_position_ranking/")
					.orderByKey()
					.startAt((position + 1).toString())
					.once('value')
					.then(ref => {
						ref.forEach(function (child) {
							admin.database().ref("/sorted_by_position_ranking/" + child.getKey()).remove();
						});
					}));
			}

			return Promise.all(innerPromises);
		}));

	return Promise.all(promises);
}