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

public class Bomb extends GameObject {
	private static final int RANGE_UP = 0;
	private static final int RANGE_DOWN = 1;
	private static final int RANGE_LEFT = 2;
	private static final int RANGE_RIGHT = 3;
	
	public static final int NORMAL = 0;
	public static final int NEARLY = 1;
	public static final int EXPLODING = 2;

	private Bitmap[] bitmaps;
	private SurfaceView surfaceView;
	private LevelGrid level;
	private int time;
	private float range;
	private boolean isExploding;
	private int actRange[];

	public float getRange() {
		return range;
	}

	public Bomb(Bitmap[] bitmaps, float x, float y, int time, float range,
			LevelGrid level, SurfaceView surfaceView) {
		super(null, x, y);
		this.bitmaps = bitmaps;
		int intx = (int) Math.rint(x);
		int inty = (int) Math.rint(y);
		this.actRange = new int[4];
		actRange[RANGE_UP] = inty;
		actRange[RANGE_DOWN] = inty;
		actRange[RANGE_LEFT] = intx;
		actRange[RANGE_RIGHT] = intx;
		this.time = time;
		this.level = level;
		this.range = range;
		this.isExploding = false;
		this.surfaceView = surfaceView;
		scale();
	}

	public void draw(Canvas canvas) {
		if (isExploding) {
			int i;
			canvas.drawBitmap(bitmaps[EXPLODING], getX() * bitmaps[EXPLODING].getWidth(),
					getY() * bitmaps[EXPLODING].getHeight(), null);
			Log.d("BOMB", "Draw explosion in X: " + getX() + " Y: " + getY());
			for (i = actRange[RANGE_UP]; i <= actRange[RANGE_DOWN]; ++i) {
				canvas.drawBitmap(bitmaps[EXPLODING], getX() * bitmaps[EXPLODING].getWidth(), i
						* bitmaps[EXPLODING].getHeight(), null);
			}
			for (i = actRange[RANGE_LEFT]; i <= actRange[RANGE_RIGHT]; ++i) {
				canvas.drawBitmap(bitmaps[EXPLODING], +i * bitmaps[EXPLODING].getWidth(),
						getY() * bitmaps[EXPLODING].getHeight(), null);
			}
		} else if (time == 0) {
			// draw nearly exploded
			canvas.drawBitmap(bitmaps[NEARLY], getX() * bitmaps[NEARLY].getWidth(),
					getY() * bitmaps[NEARLY].getHeight(), null);
		} else {
			// draw normal bomb
			canvas.drawBitmap(bitmaps[NORMAL], getX() * bitmaps[NORMAL].getWidth(),
					getY() * bitmaps[NORMAL].getHeight(), null);
		}

	}

	/**
	 * Use when surface size changes.
	 */
	public void scale() {
		int newWidth = surfaceView.getWidth() / level.getRowSize();
		int newHeight = surfaceView.getHeight() / level.getCollSize();
		for (int bmp = 0; bmp < bitmaps.length; ++bmp) {
			bitmaps[bmp] = (Bitmap.createScaledBitmap(bitmaps[bmp], newWidth,
					newHeight, false));
		}

		Log.d("SCALE",
				"ScaledF width: " + newWidth + " real: "
						+ bitmaps[0].getWidth() + " ScaledF height: "
						+ newHeight + " real: " + bitmaps[0].getHeight());
	}

	public float tick() {
		// time = (float) (time - 1.0f);
		return time--;
	}

	/**
	 * Play the explosion sound, find the at most up, down, left and right act
	 * range and destroy all in range obstacles. A bomb stops its explosion in a
	 * certain direction when encountering a wall or an obstacle. Note: Updates
	 * actRange field.
	 * 
	 * @param context
	 * @return the actRange
	 */
	public int[] explode(Context context) {
		SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		int iTmp = sp.load(context, R.raw.sound_bomb_1, 1);
		sp.play(iTmp, 1, 1, 0, 0, 1);
		MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.sound_bomb_1);
		mPlayer.start();

		Log.d("BOMB", "Draw explosion in X: " + getX() + " Y: " + getY());
		// Find act range up
		for (int i = 0; i <= range; ++i) {
			if (getY() - i < 0) {
				break;
			}
			int y = (int) Math.rint(getY() - i);
			int x = (int) Math.rint(getX());
			if (level.getGridCell(y, x) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_UP] = y;
				if (level.getGridCell(y, x) == LevelGrid.OBSTACLE) {
					level.setGridCell(y, x, LevelGrid.EMPTY);
					break;
				}
			}
		}
		// Find act range down
		for (int i = 0; i <= range; ++i) {
			if (getY() + i > level.getCollSize() - 1) {
				break;
			}
			int y = (int) Math.rint(getY() + i);
			int x = (int) Math.rint(getX());
			if (level.getGridCell(y, x) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_DOWN] = y;
				if (level.getGridCell(y, x) == LevelGrid.OBSTACLE) {
					level.setGridCell(y, x, LevelGrid.EMPTY);
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
			if (level.getGridCell(y, x) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_LEFT] = x;
				if (level.getGridCell(y, x) == LevelGrid.OBSTACLE) {
					level.setGridCell(y, x, LevelGrid.EMPTY);
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
			if (level.getGridCell((int) Math.rint(getY()), x) == LevelGrid.WALL) {
				break;
			} else {
				actRange[RANGE_RIGHT] = x;
				if (level.getGridCell(y, x) == LevelGrid.OBSTACLE) {
					level.setGridCell(y, x, LevelGrid.EMPTY);
					break;
				}
			}
		}
		Log.d("BOMB",
				"Bomb exploded in X: " + this.getX() + " Y: " + this.getY());
		isExploding = true;
		return actRange;
	}
}
