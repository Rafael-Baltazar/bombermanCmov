package com.example.bombermancmov.model;

import com.example.bombermancmov.model.component.DrawableComponent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Character extends GameObject {
	private static final String TAG = Character.class.getSimpleName();
	private float speed;
	private int speedx = 0;
	private int speedy = 0;
	private float points;
	private boolean isAlive;

	private LevelGrid grid;

	/**
	 * Handles draw method.
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
	 * @param grid
	 * @param game
	 *            TODO
	 */
	public Character(Bitmap bitmap[], float x, float y, float speed, LevelGrid grid, Game game, boolean isAlive) {
		super(x, y);
		this.speed = speed;
		this.points = 0;
		this.grid = grid;
		this.isAlive = isAlive;
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

	public void tryMoveDown() {
		drawableComponent.setActiveBitmapIndex(FRONT);
		speedy = +1;
		speedx = 0;
	}

	public void tryMoveUp() {
		drawableComponent.setActiveBitmapIndex(BACK);
		speedy = -1;
		speedx = 0;
	}

	public void tryMoveLeft() {
		drawableComponent.setActiveBitmapIndex(LEFT);
		speedy = 0;
		speedx = -1;
	}

	public void tryMoveRight() {
		drawableComponent.setActiveBitmapIndex(RIGHT);
		speedy = 0;
		speedx = +1;
	}

	public void stop() {
		speedx = 0;
		speedy = 0;
	}

	/**
	 * Move to position (x,y). Does not check the cells between the origin and
	 * the destination cells.
	 * 
	 * @param x
	 * @param y
	 */
	public void move(float x, float y) {
		int intX = (int) Math.rint(x);
		int intY = (int) Math.rint(y);
		if (!isCollidable(intX, intY)) {
			setX(x);
			setY(y);
		}
	}

	private boolean isCollidable(int x, int y) {
		return grid.getGridCell(x, y) == LevelGrid.WALL
				|| grid.getGridCell(x, y) == LevelGrid.OBSTACLE;
	}

	public void update(long timePassed) {
		float distance = (float) speed * timePassed / 1000.0f;
		int x = (int) Math.rint(getX());
		int y = (int) Math.rint(getY());
		/* Search for collisions, one cell at a time. */
		for (int i = 0; i < distance; i++) {
			if (isCollidable(x + i * speedx, y + i * speedy)) {
				distance = i - 1;
				break;
			}
		}
		move(getX() + distance * speedx, getY() + distance * speedy);
	}

	/**
	 * Draws the character on the canvas.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		drawableComponent.draw(canvas);
	}

	public void placeBomb() {
	}

	public void kill() {
		this.setAlive(false);
		Log.d(TAG, "Character died.");
	}
	
	
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

}
