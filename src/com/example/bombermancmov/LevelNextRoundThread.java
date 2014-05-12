package com.example.bombermancmov;

import com.example.bombermancmov.model.Game;

public class LevelNextRoundThread extends Thread {
	private Game level;
	private int roundTime;
	private boolean running = true;

	public LevelNextRoundThread(Game level, int roundTime) {
		this.level = level;
		this.roundTime = roundTime;
	}

	public void setRunning(boolean value) { running = value; }
	
	@Override
	public void run() {
		while (level.nextRound() && running) {
			try {
				Thread.sleep(roundTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
