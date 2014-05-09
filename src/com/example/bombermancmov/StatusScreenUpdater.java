package com.example.bombermancmov;

import android.app.Activity;
import android.widget.TextView;

public class StatusScreenUpdater {
	private TextView playerNameTextView, playerScoreTextView, timeLeftTextView,
			numPlayersTextView;
	private Activity act;

	/**
	 * @param playerNameTextView
	 * @param playerScoreTextView
	 * @param timeLeftTextView
	 * @param numPlayersTextView
	 * @param act TODO
	 */
	public StatusScreenUpdater(TextView playerNameTextView,
			TextView playerScoreTextView, TextView timeLeftTextView,
			TextView numPlayersTextView, Activity act) {
		this.playerNameTextView = playerNameTextView;
		this.playerScoreTextView = playerScoreTextView;
		this.timeLeftTextView = timeLeftTextView;
		this.numPlayersTextView = numPlayersTextView;
		this.act = act;
	}
	
	public void runOnUiThread(Runnable run) {
		act.runOnUiThread(run);
	}

	public void setPlayerName(String playerName) {
		playerNameTextView.setText(playerName);
	}

	public void setPlayerScore(float points) {
		playerScoreTextView.setText(Float.toString(points));
	}

	public void setTimeLeft(float timeLeft) {
		timeLeftTextView.setText(Float.toString(timeLeft));
	}

	public void setNumPlayers(int numPlayers) {
		numPlayersTextView.setText(Integer.toString(numPlayers));
	}
}
