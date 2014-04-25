package com.example.bombermancmov.model;

import com.example.bombermancmov.model.component.DrawableComponent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
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
	
	public static int FRONT = 0;
	public static int LEFT = 1;
	public static int RIGHT = 2;
	public static int BACK = 3;
	
	/**
	 * @param x
	 * @param y
	 * @param speed
	 * @param level
	 * @param surfaceView
	 */
	public Character(Bitmap bitmap[], float x, float y, float speed, LevelGrid level, SurfaceView surfaceView) {
		super(x, y);
		this.speed = speed;
		this.level = level;
		this.points = 0;
		this.surfaceView = surfaceView;
		
		drawableComponent = new DrawableComponent(this, bitmap, FRONT);
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
	
	private int speedx = 0;
	private int speedy = 0;
	
	public void moveDown(long timePassed){
		drawableComponent.setActiveBitmapIndex(FRONT);
		speedy = +1;
		speedx = 0;
		float curSpeed = (float) speed*timePassed/1000.0f;
		move(getX()+curSpeed*speedx, getY()+curSpeed*speedy);
	}
	
	public void moveUp(long timePassed){
		drawableComponent.setActiveBitmapIndex(BACK);
		speedy = -1;
		speedx = 0;
		float curSpeed = (float) speed*timePassed/1000.0f;
		move(getX()+curSpeed*speedx, getY()+curSpeed*speedy);
	}
	
	public void moveLeft(long timePassed){
		drawableComponent.setActiveBitmapIndex(LEFT);
		speedy = 0;
		speedx = -1;
		float curSpeed = (float) speed*timePassed/1000.0f;
		move(getX()+curSpeed*speedx, getY()+curSpeed*speedy);
	}
	
	public void moveRight(long timePassed){
		drawableComponent.setActiveBitmapIndex(RIGHT);
		speedy = 0;
		speedx = +1;
		float curSpeed = (float) speed*timePassed/1000.0f;
		move(getX()+curSpeed*speedx, getY()+curSpeed*speedy);
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
		if (level.getGridCell(intY, intX) != LevelGrid.WALL && level.getGridCell(intY, intX) != LevelGrid.OBSTACLE) {
			setX(x);
			setY(y);
			Log.d("CHAR", "X: " + getX() + " :::: Y: "+ getY());
			return true;
		} else {
			return false;
		}
	}
	
	float movedDist = 0;
	
	public boolean update(long timePassed) {
		if(movedDist > speed){
			speedx = 0;
			speedy = 0;
			movedDist = 0;
			return false;
		}
		float curSpeed = (float) speed*timePassed/1000.0f;
		movedDist += curSpeed;
		return move(getX()+curSpeed*speedx, getY()+curSpeed*speedy);
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
