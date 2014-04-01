package com.example.bombermancmov.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {
	private Bitmap bitmap;
	private float x, y;
	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public GameObject(Bitmap bitmap, float x, float y) {
		super();
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
	}
}
