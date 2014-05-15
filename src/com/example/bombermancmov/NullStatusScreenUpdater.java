package com.example.bombermancmov;

public class NullStatusScreenUpdater extends StatusScreenUpdater {

	/**
	 * Used in master-only game, where there is no status screen.
	 */
	public NullStatusScreenUpdater() {
		super(null, null, null, null, null);
	}

	@Override
	public void runOnUiThread(Runnable run) {
	}

	@Override
	public void setPlayerName(String playerName) {
	}

	@Override
	public void setPlayerScore(int points) {
	}

	@Override
	public void setTimeLeft(int timeLeft) {
	}

	@Override
	public void setNumPlayers(int numPlayers) {
	}

}
