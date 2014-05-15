package com.example.bombermancmov.model;

import java.util.List;

import android.graphics.Bitmap;

public class Droid extends Character {
	private static final String TAG = Droid.class.getSimpleName();
	/** Change the direction of the droids every TICK milliseconds. */
	private static long TIME_TO_MOVE = 1500;
	private long timeToProcess = 0;
	
	private Game mGame;

	public Droid(Bitmap[] bitmap, float x, float y, float speed, Game game, boolean isAlive) {
		super(bitmap, x, y, speed, game.getLevel().getGrid(), game, isAlive);
		mGame = game;
	}

	public void update(long timePassed) {
		super.update(timePassed);
		tryKill();
	}

	private void tryKill() {
		int intX = (int) Math.rint(getX());
		int intY = (int) Math.rint(getY());
		List<Character> players = mGame.getPlayersByPos(intX, intY);
		for (Character p : players) {
			p.kill();
		}
	}
	
	public void updateDroid(long timePassed) {
		timeToProcess += timePassed;
		if (timeToProcess > TIME_TO_MOVE) {
			this.tryMoveRandomly();
			timeToProcess = 0;
		}
	}

	public void tryMoveRandomly() {
		double rnd = Math.random();
		if (rnd < 0.25) {
			this.tryMoveDown();
		} else if (rnd < 0.5) {
			this.tryMoveLeft();
		} else if (rnd < 0.75) {
			this.tryMoveRight();
		} else {
			this.tryMoveUp();
		}
	}
}
