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
		super(x, y);
		this.bitmaps = bitmaps;
		this.actRange = new int[4];
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
			canvas.drawBitmap(bitmaps[2], getX() * bitmaps[2].getWidth(),
					getY() * bitmaps[2].getHeight(), null);
			Log.d("BOMB", "Draw explosion in X: " + getX() + " Y: " + getY());
			for (i = actRange[0]; i <= actRange[1]; ++i) {
				canvas.drawBitmap(bitmaps[2], getX() * bitmaps[2].getWidth(), i
						* bitmaps[2].getHeight(), null);
			}
			for (i = actRange[2]; i <= actRange[3]; ++i) {
				canvas.drawBitmap(bitmaps[2], +i * bitmaps[2].getWidth(),
						getY() * bitmaps[2].getHeight(), null);
			}
		} else if (time == 0) {
			// draw nearly exploded
			canvas.drawBitmap(bitmaps[1], getX() * bitmaps[1].getWidth(),
					getY() * bitmaps[1].getHeight(), null);
		} else {
			// draw normal bomb
			canvas.drawBitmap(bitmaps[0], getX() * bitmaps[0].getWidth(),
					getY() * bitmaps[0].getHeight(), null);
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
