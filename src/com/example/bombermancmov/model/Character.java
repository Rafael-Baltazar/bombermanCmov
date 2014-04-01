package com.example.bombermancmov.model;

import android.graphics.Bitmap;

public class Character extends GameObject {
	private float speed = 0.0f;
	
	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public Character(Bitmap bitmap, float x, float y) {
		super(bitmap, x, y);
	}
	
	/**
	 * Move to position (x,y).
	 * 
	 * @param x
	 * @param y
	 */
	public void move(float x, float y) {
		setX(x);
		setY(y);
	}
}
