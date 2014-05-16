package com.example.bombermancmov.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.bombermancmov.model.component.DrawableExplosionComponent;
import com.example.bombermancmov.model.component.SoundComponent;

public class Bomb extends GameObject {
	public static final int RANGE_UP = 0;
	public static final int RANGE_DOWN = 1;
	public static final int RANGE_LEFT = 2;
	public static final int RANGE_RIGHT = 3;

	public static final int NORMAL = 0;
	public static final int NEARLY = 1;
	public static final int EXPLODING = 2;

	private static final float TIME_TO_NEARLY = 700;
	private LevelGrid level;
	private Game game;
	private Character owner;
	private int ownerId;
	private int timeToExplode;
	private float explosionDuration;
	private float range;
	private boolean isExploding;
	private int actRange[] = null;
	// Explosion sound
	private SoundComponent mSoundComponent;

	/** Handles draw method. */
	DrawableExplosionComponent drawableComponent;

	public float getRange() {
		return range;
	}

	public Bomb(Bitmap[] bitmaps, Character owner, int ownerId, float x, float y, Game game,
			SoundComponent soundComponent) {
		super(x, y);
		this.timeToExplode = game.getLevel().getExplosionTimeout() * 1000;
		this.explosionDuration = game.getLevel().getExplosionDuration() * 1000;
		this.level = game.getLevel().getGrid();
		this.range = game.getLevel().getExplosionRange();
		this.game = game;
		this.isExploding = false;
		this.owner = owner;
		this.ownerId = ownerId;
		// Explosion Sound load
		mSoundComponent = soundComponent;
		drawableComponent = new DrawableExplosionComponent(this, bitmaps,
				NORMAL);
	}

	public void draw(Canvas canvas) {
		if (isExploding) {
			drawableComponent.draw(canvas, actRange);
		} else {
			drawableComponent.draw(canvas);
		}
	}

	public float update(long timePassed) {
		timeToExplode -= timePassed;
		if (timeToExplode > TIME_TO_NEARLY) {
			drawableComponent.setActiveBitmapIndex(NORMAL);
		} else if (timeToExplode > 0) {
			drawableComponent.setActiveBitmapIndex(NEARLY);
		} else if (timeToExplode > -explosionDuration) {
			Log.d("Bomb", timeToExplode + " : " + -explosionDuration);
			if(actRange == null) {
				initActRange();
				explode();
				game.explosionCollision(this, actRange);
			}
			drawableComponent.setActiveBitmapIndex(EXPLODING);
			isExploding = true;
		}
		return timeToExplode;
	}

	private void initActRange() {
		int intx = (int) Math.rint(getX());
		int inty = (int) Math.rint(getY());
		this.actRange = new int[4];
		actRange[RANGE_UP] = inty;
		actRange[RANGE_DOWN] = inty;
		actRange[RANGE_LEFT] = intx;
		actRange[RANGE_RIGHT] = intx;
	}

	/**
	 * Play the explosion sound, find the at most up, down, left and right act
	 * range and destroy all in range obstacles. A bomb stops its explosion in a
	 * certain direction, when encountering a wall or an obstacle. Note: Updates
	 * actRange field.
	 * 
	 * @return the actRange
	 */
	public int[] explode() {
		mSoundComponent.play();

		Log.d("BOMB", "Draw explosion in X: " + getX() + " Y: " + getY());
		// Find act range up
		for (int i = 0; i <= range; ++i) {
			if (getY() - i < 0) {
				break;
			}
			int y = (int) Math.rint(getY() - i);
			int x = (int) Math.rint(getX());
			if (level.getGridCell(x, y) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_UP] = y;
				if (level.getGridCell(x, y) == LevelGrid.OBSTACLE) {
					level.setGridCell(x, y, LevelGrid.EMPTY);
					break;
				}
			}
		}
		// Find act range down
		for (int i = 0; i <= range; ++i) {
			if (getY() + i > level.getColSize() - 1) {
				break;
			}
			int y = (int) Math.rint(getY() + i);
			int x = (int) Math.rint(getX());
			if (level.getGridCell(x, y) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_DOWN] = y;
				if (level.getGridCell(x, y) == LevelGrid.OBSTACLE) {
					level.setGridCell(x, y, LevelGrid.EMPTY);
					break;
				}
			}
		}
		// Find act range left
		for (int i = 0; i <= range; ++i) {
			if (getX() - i < 0) {
				break;
			}
			int y = (int) Math.rint(getY());
			int x = (int) Math.rint(getX() - i);
			if (level.getGridCell(x, y) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_LEFT] = x;
				if (level.getGridCell(x, y) == LevelGrid.OBSTACLE) {
					level.setGridCell(x, y, LevelGrid.EMPTY);
					break;
				}
			}
		}
		// Find act range right
		for (int i = 0; i <= range; ++i) {
			if (getX() + i > level.getRowSize()) {
				break;
			}
			int y = (int) Math.rint(getY());
			int x = (int) Math.rint(getX() + i);
			if (level.getGridCell(x, y) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_RIGHT] = x;
				if (level.getGridCell(x, y) == LevelGrid.OBSTACLE) {
					level.setGridCell(x, y, LevelGrid.EMPTY);
					break;
				}
			}
		}
		Log.d("BOMB",
				"Bomb exploded in X: " + this.getX() + " Y: " + this.getY());
		isExploding = true;
		return actRange;
	}

	public Character getOwner() {
		return owner;
	}

	public float getExplosionDuration() {
		return explosionDuration;
	}
	public int getOwnerId(){
		return ownerId;
	}

}
