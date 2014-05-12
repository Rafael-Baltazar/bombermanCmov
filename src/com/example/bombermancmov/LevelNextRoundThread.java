package com.example.bombermancmov;

import com.example.bombermancmov.model.Game;

public class LevelNextRoundThread extends Thread {
	private Game game;
	private int roundTime;
	private boolean running = true;

	public LevelNextRoundThread(Game game, int roundTime) {
		this.game = game;
		this.roundTime = roundTime;
	}

	public void setRunning(boolean value) { running = value; }
	
	@Override
	public void run() {
		while (game.nextRound() && running && !game.isFinished()) {
			try {
				Thread.sleep(roundTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
