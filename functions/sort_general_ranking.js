'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sortRank = functions.database.ref('/ranking_collect_by_user')
	.onWrite(event => calculate_scoreboard(event))
	.onUpdate(event => calculate_scoreboard(event));

function calculate_scoreboard(event) {
	const rankingRef = event.data.ref.orderByChild('score'); // this is the colection raking
	const sortedRankRef = functions.database.ref('sorted_general_ranking');

	for (int i = 0; i < rankingRef.length; i++) {
	
		var sortedValue = sortedRankRef.get(rankingRef[i].uid);

		var before = i == 0 ? null : rankingRef[i - 1];
		var after = i == rankingRef.length ? null : rankingRef[i + 1];

		sortedRankRef.setValue({
			position: i + 1,
			userId: rankingRef[i].uid,
			score: rankingRef[i].score,
			before: before,
			after: after
		});
	}
}