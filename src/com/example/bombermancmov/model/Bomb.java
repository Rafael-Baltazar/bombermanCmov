package com.example.bombermancmov.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceView;

import com.example.bombermancmov.R;
import com.example.bombermancmov.model.component.DrawableComponent;
import com.example.bombermancmov.model.component.DrawableExplosionComponent;

public class Bomb extends GameObject {
	public static int NORMAL = 0;
	public static int NEARLY= 1;
	public static int EXPLODING = 2;

	private Bitmap[] bitmaps;
	private SurfaceView surfaceView;
	private LevelGrid level;
	private int timeToExplode;
	private float range;
	private boolean isExploding;
	private int actRange[];
	
	/**
	 * Handles draw and scale methods.
	 */
	DrawableExplosionComponent drawableComponent;

	public float getRange() {
		return range;
	}

	public Bomb(Bitmap[] bitmaps, float x, float y, int time, float range,
			LevelGrid level, SurfaceView surfaceView) {
		super(x, y);
		this.bitmaps = bitmaps;
		this.actRange = new int[4];
		this.timeToExplode = time;
		this.level = level;
		this.range = range;
		this.isExploding = false;
		this.surfaceView = surfaceView;
		
		drawableComponent = new DrawableExplosionComponent(this, bitmaps, NORMAL);
		scale();
	}

	public void draw(Canvas canvas) {
		if (isExploding) {
			drawableComponent.draw(canvas, range);
		} else {
			drawableComponent.draw(canvas);
		}
	}

	/**
	 * Use when surface size changes.
	 */
	public void scale() {
		int newWidth = surfaceView.getWidth() / level.getRowSize();
		int newHeight = surfaceView.getHeight() / level.getCollSize();
		drawableComponent.scale(newWidth, newHeight);
	}

	public float tick() {
		if(timeToExplode > 1) {
			drawableComponent.setActiveBitmapIndex(NORMAL);
		} else if(timeToExplode == 1) {
			drawableComponent.setActiveBitmapIndex(NEARLY);
		} else {
			drawableComponent.setActiveBitmapIndex(EXPLODING);
			isExploding = true;
		}
		return timeToExplode--;
	}

	public int[] explode(Context context) {
		boolean blockUp = false, blockDown = false, blockLeft = false, blockRight = false;
		SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		int iTmp = sp.load(context, R.raw.sound_bomb_1, 1);
		sp.play(iTmp, 1, 1, 0, 0, 1);
		MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.sound_bomb_1);
		mPlayer.start();

		Log.d("BOMB", "Draw explosion in X: " + getX() + " Y: " + getY());
		for (int i = 0; i <= range; ++i) {
			// up
			if (!blockUp
					&& getY() - i > 0
					&& (level.getGridCell((int) Math.rint(getY() - i),
							(int) Math.rint(getX())) == LevelGrid.EMPTY)) {
				actRange[0] = (int) Math.rint(getY() - i);
			} else {
				if (!blockUp
						&& level.getGridCell((int) Math.rint(getY() - i),
								(int) Math.rint(getX())) == LevelGrid.OBSTACLE) {
					actRange[0] = (int) Math.rint(getY() - i);
					level.setGridCell((int) Math.rint(getY() - i),
							(int) Math.rint(getX()), LevelGrid.EMPTY);
				}
				blockUp = true;
			}

			// down
			if (!blockDown
					&& getY() + i > 0
					&& (level.getGridCell((int) Math.rint(getY() + i),
							(int) Math.rint(getX())) == LevelGrid.EMPTY)) {
				actRange[1] = (int) Math.rint(getY() + i);

			} else {

				if (!blockDown
						&& level.getGridCell((int) Math.rint(getY() + i),
								(int) Math.rint(getX())) == LevelGrid.OBSTACLE) {
					actRange[1] = (int) Math.rint(getY() + i);
					level.setGridCell((int) Math.rint(getY() + i),
							(int) Math.rint(getX()), LevelGrid.EMPTY);
				}
				blockDown = true;
			}

			// left
			if (!blockLeft
					&& getX() - i > 0
					&& (level.getGridCell((int) Math.rint(getY()),
							(int) Math.rint(getX() - i)) == LevelGrid.EMPTY)) {
				actRange[2] = (int) Math.rint(getX() - i);

			} else {
				if (!blockLeft
						&& level.getGridCell((int) Math.rint(getY()),
								(int) Math.rint(getX() - i)) == LevelGrid.OBSTACLE) {
					actRange[2] = (int) Math.rint(getX() - i);
					level.setGridCell((int) Math.rint(getY()),
							(int) Math.rint(getX() - i), LevelGrid.EMPTY);
				}
				blockLeft = true;
			}
			// right
			if (!blockRight
					&& getX() + i > 0
					&& (level.getGridCell((int) Math.rint(getY()),
							(int) Math.rint(getX() + i)) == LevelGrid.EMPTY)) {
				actRange[3] = (int) Math.rint(getX() + i);
			} else {
				if (!blockRight
						&& level.getGridCell((int) Math.rint(getY()),
								(int) Math.rint(getX() + i)) == LevelGrid.OBSTACLE) {
					actRange[3] = (int) Math.rint(getX() + i);
					level.setGridCell((int) Math.rint(getY()),
							(int) Math.rint(getX() + i), LevelGrid.EMPTY);
				}
				blockRight = true;
			}

			Log.d("BOMB",
					"Bomb exploded in X: " + this.getX() + " Y: " + this.getY());
		}
		isExploding = true;
		return actRange;
	}
}
