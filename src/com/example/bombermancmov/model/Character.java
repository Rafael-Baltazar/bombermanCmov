package com.example.bombermancmov.model;

import com.example.bombermancmov.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

public class Character extends GameObject {
	private float speed = 0.3f;
	private LevelGrid level;
	private Bitmap[] bitmaps;
	private SurfaceView surfaceView;
	
	private static int FRONT = 0;
	private static int LEFT = 1;
	private static int RIGHT = 2;
	private static int BACK = 3;
	
	private int activeSprite;

	/**
	 * @param x
	 * @param y
	 * @param speed
	 * @param level
	 * @param surfaceView
	 */
	public Character(float x, float y, float speed, LevelGrid level, SurfaceView surfaceView) {
		super(null, x, y);
		this.speed = speed;
		this.level = level;
		this.surfaceView = surfaceView;
		bitmaps = new Bitmap[4];
		bitmaps[FRONT] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_0);
		bitmaps[LEFT] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_1);
		bitmaps[RIGHT] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_2);
		bitmaps[BACK] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_3);
		activeSprite = FRONT;
	}
	
	public Bitmap getActiveSprite(){
		return bitmaps[activeSprite];
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
	
	public boolean moveDown(){
		activeSprite = FRONT;
		return move(getX(), getY()+speed);
	}
	
	public boolean moveUp(){
		activeSprite = BACK;
		return move(getX(), getY()-speed);
	}
	
	public boolean moveLeft(){
		activeSprite = LEFT;
		return move(getX()-speed, getY());
	}
	
	public boolean moveRight(){
		activeSprite = RIGHT;
		return move(getX()+speed, getY());
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
	
	public void draw(Canvas canvas){
		Bitmap activeBitMap = bitmaps[activeSprite];
		canvas.drawBitmap(activeBitMap, getX()*activeBitMap.getWidth(), getY()*activeBitMap.getHeight(), null);
	}
	
	public void scale() {
		int newWidth = surfaceView.getWidth() / level.getRowSize();
		int newHeight = surfaceView.getHeight() / level.getCollSize();
		bitmaps[FRONT] =  Bitmap.createScaledBitmap(bitmaps[FRONT], newWidth, newHeight, false);
		bitmaps[LEFT] =  Bitmap.createScaledBitmap(bitmaps[LEFT], newWidth, newHeight, false);
		bitmaps[RIGHT] =  Bitmap.createScaledBitmap(bitmaps[RIGHT], newWidth, newHeight, false);
		bitmaps[BACK] =  Bitmap.createScaledBitmap(bitmaps[BACK], newWidth, newHeight, false);
		Log.d("SCALE", "ScaledF width: " + newWidth + " real: " + bitmaps[FRONT].getWidth() + 
				" ScaledF height: " + newHeight + " real: " + bitmaps[FRONT].getHeight());
		Log.d("SCALE", "ScaledL width: " + newWidth + " real: " + bitmaps[LEFT].getWidth() + 
				" ScaledL height: " + newHeight + " real: " + bitmaps[LEFT].getHeight());
		Log.d("SCALE", "ScaledR width: " + newWidth + " real: " + bitmaps[RIGHT].getWidth() + 
				" ScaledR height: " + newHeight + " real: " + bitmaps[RIGHT].getHeight());
		Log.d("SCALE", "ScaledB width: " + newWidth + " real: " + bitmaps[BACK].getWidth() + 
				" ScaledB height: " + newHeight + " real: " + bitmaps[BACK].getHeight());
	}
}
