package com.example.bombermancmov.model;

import com.example.bombermancmov.R;
import com.example.bombermancmov.model.component.DrawableComponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class Character extends GameObject {
	private float speed = 0.3f;
	private LevelGrid level;
	private SurfaceView surfaceView;
	private float points;
	
	/**
	 * Handles draw and scale methods.
	 */
	DrawableComponent drawableComponent;
	
	private String name;
	
	private static int FRONT = 0;
	private static int LEFT = 1;
	private static int RIGHT = 2;
	private static int BACK = 3;
	
	/**
	 * @param x
	 * @param y
	 * @param speed
	 * @param level
	 * @param surfaceView
	 */
	public Character(float x, float y, float speed, LevelGrid level, SurfaceView surfaceView) {
		super(x, y);
		this.speed = speed;
		this.level = level;
		this.points = 0;
		this.surfaceView = surfaceView;
		
		Bitmap bitmaps[] = new Bitmap[4];
		bitmaps[FRONT] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_0);
		bitmaps[LEFT] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_1);
		bitmaps[RIGHT] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_2);
		bitmaps[BACK] =  BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.c0_3);
		drawableComponent = new DrawableComponent(this, bitmaps, FRONT);
	}
	
	public String getName() {
		return name;
	}

	public float getPoints() {
		return points;
	}

	public void setPoints(float points) {
		this.points = points;
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
		drawableComponent.setActiveBitmapIndex(FRONT);
		return move(getX(), getY()+speed);
	}
	
	public boolean moveUp(){
		drawableComponent.setActiveBitmapIndex(BACK);
		return move(getX(), getY()-speed);
	}
	
	public boolean moveLeft(){
		drawableComponent.setActiveBitmapIndex(LEFT);
		return move(getX()-speed, getY());
	}
	
	public boolean moveRight(){
		drawableComponent.setActiveBitmapIndex(RIGHT);
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
		if (level.getGridCell(intY, intX) == LevelGrid.EMPTY) {
			setX(x);
			setY(y);
			return true;
		} else {
			return false;
		}
	}
	
	public void draw(Canvas canvas){
		drawableComponent.draw(canvas);
	}
	
	public void scale() {
		int newWidth = surfaceView.getWidth() / level.getRowSize();
		int newHeight = surfaceView.getHeight() / level.getCollSize();
		drawableComponent.scale(newWidth, newHeight);
	}
	
	public void placeBomb(){
	}
	
}
