package com.example.bombermancmov.model;

import android.graphics.Bitmap;
import android.view.SurfaceView;

public class Droid extends Character {
	
	public Droid(Bitmap[] bitmap, float x, float y, float speed,
			LevelGrid level, SurfaceView surfaceView) {
		super(bitmap, x, y, speed, level, surfaceView);
	}

	public void moveRandomly() {
		double rnd = Math.random();
		if(rnd < 0.25) {
			this.moveDown(10);
		} else if(rnd < 0.5) {
			this.moveLeft(10);
		} else if(rnd < 0.75) {
			this.moveRight(10);
		} else {
			this.moveUp(10);
		}
	}
}
