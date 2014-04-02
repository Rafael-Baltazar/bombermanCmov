package com.example.bombermancmov.model;

import com.example.bombermancmov.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class Character extends GameObject {
	private float speed = 0.0f;
	private LevelGrid level;
	private Bitmap[] bitmaps;
	private SurfaceView surfaceHolder;
	
	private static int FRONT = 0;
	private static int LEFT = 1;
	private static int RIGHT = 2;
	private static int BACK = 3;
	
	private int activeSprite;

	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public Character(float x, float y, float speed, LevelGrid level, SurfaceView surfaceHolder) {
		super(null, x, y);
		this.speed = speed;
		this.level = level;
		this.surfaceHolder = surfaceHolder;
		bitmaps = new Bitmap[4];
		bitmaps[FRONT] =  BitmapFactory.decodeResource(surfaceHolder.getResources(), R.drawable.c0_0);
		bitmaps[LEFT] =  BitmapFactory.decodeResource(surfaceHolder.getResources(), R.drawable.c0_1);
		bitmaps[RIGHT] =  BitmapFactory.decodeResource(surfaceHolder.getResources(), R.drawable.c0_2);
		bitmaps[BACK] =  BitmapFactory.decodeResource(surfaceHolder.getResources(), R.drawable.c0_3);
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
		return move(getX(), getY()-1);
	}
	
	public boolean moveUp(){
		activeSprite = BACK;
		return move(getX(), getY()+1);
	}
	
	public boolean moveLeft(){
		activeSprite = LEFT;
		return move(getX()-1, getY());
	}
	
	public boolean moveRight(){
		activeSprite = RIGHT;
		return move(getX()+1, getY());
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
		
		Bitmap wallBitMap = bitmaps[activeSprite];
		
		int newWight = surfaceHolder.getWidth() / level.getRowSize();
		int newHeight = surfaceHolder.getHeight() / level.getCollSize();
		
		canvas.drawBitmap(Bitmap.createScaledBitmap(wallBitMap, newWight, newHeight, false), getX()*newWight, getY()*newHeight , null);
	}
}
