package com.example.bombermancmov;

import android.app.Activity;
import android.widget.TextView;

public class StatusScreenUpdater {
	private TextView playerNameTextView;
	private TextView playerScoreTextView;
	private TextView timeLeftTextView;
	private TextView numPlayersTextView;
	private Activity act;

	/**
	 * @param act
	 */
	public StatusScreenUpdater(Activity act) {
		this.act = act;
		this.playerNameTextView = (TextView) act.findViewById(R.id.playerName);
		this.playerScoreTextView = (TextView) act
				.findViewById(R.id.playerScore);
		this.timeLeftTextView = (TextView) act.findViewById(R.id.timeLeft);
		this.numPlayersTextView = (TextView) act
				.findViewById(R.id.numberPlayers);
	}

	public void runOnUiThread(Runnable run) {
		act.runOnUiThread(run);
	}

	public void setPlayerName(String playerName) {
		playerNameTextView.setText(playerName);
	}

	public void setPlayerScore(int points) {
		playerScoreTextView.setText(Integer.toString(points));
	}

	public void setTimeLeft(int timeLeft) {
		timeLeftTextView.setText(Integer.toString(timeLeft) + "s");
	}

	public void setNumPlayers(int numPlayers) {
		numPlayersTextView.setText(Integer.toString(numPlayers));
	}
}
