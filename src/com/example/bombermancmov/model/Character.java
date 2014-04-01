package com.example.bombermancmov.model;

import android.graphics.Bitmap;

public class Character extends GameObject {
	private float speed = 0.0f;
	private LevelGrid level;

	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public Character(Bitmap bitmap, float x, float y, float speed, LevelGrid level) {
		super(bitmap, x, y);
		this.speed = speed;
		this.level = level;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public LevelGrid getLevel() {
		return level;
	}

	public void setLevel(LevelGrid level) {
		this.level = level;
	}

	/**
	 * Move to position (x,y). Does not check the cells between the origin and
	 * the destination cells.
	 * 
	 * @param x
	 * @param y
	 * @return true, if the character moved. Otherwise, false.
	 */
	public boolean move(float x, float y) {
		int intX = (int) Math.rint(x);
		int intY = (int) Math.rint(y);
		if (level.getGridCell(intX, intY) == LevelGrid.EMPTY) {
			setX(x);
			setY(y);
			return true;
		} else {
			return false;
		}
	}
}
