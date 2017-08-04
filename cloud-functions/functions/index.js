'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sortRankOnWrite = functions.database.ref('/ranking_collect_by_user/{pushId}').onWrite(event => calculate_scoreboard(event));

function calculate_scoreboard(event) {

	if (!event.data.exists()) {
		// The data is being deleted
		console.log("removing " + event.params.pushId + " from sorted rank");
		admin.database().ref('/sorted_general_ranking/' + event.params.pushId).remove();
		// admin.database().ref('/sorted_general_ranking/' + event.getKey()).on('value')
		// 	.then(ref => {
		// 		ref.remove();
		// 	})
		// 	.catch(err => {
		// 		console.log(err.stack);
		// 		return;
		// 	});
	}

	admin.database().ref('/ranking_collect_by_user').orderByChild('score').once('value')
		.then(rankingRef => {
			var entities = [];
			var count = 0;

			rankingRef.forEach(function(child) {
				entities[count] = child;
				count++;
			});

			entities.reverse(); // Reverse because it comes in ascend order
			
			for (var i = 0; i < entities.length; i++) {
				console.log("entity: " + entities[i].getKey());

				var before = i == 0 ? null : entities[i - 1].getKey();
				var after = i == entities.length - 1 ? null : entities[i + 1].getKey();

				admin.database().ref('/sorted_general_ranking/' + entities[i].getKey()).set({
					position: i + 1,
					score: entities[i].val().score,
					before: before,
					after: after
				});
			}

			return;
		})
		.catch(err => {
			console.log(err.stack);
			return;
		});
}
