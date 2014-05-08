package com.example.bombermancmov.model;

import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class Droid extends Character {
	private static final String TAG = Droid.class.getSimpleName();
	
	private Game mGame;

	public Droid(Bitmap[] bitmap, float x, float y, float speed, Game game) {
		super(bitmap, x, y, speed, game.getLevel().getGrid());
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
