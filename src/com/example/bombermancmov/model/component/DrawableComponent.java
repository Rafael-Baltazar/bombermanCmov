package com.example.bombermancmov.model.component;

import com.example.bombermancmov.model.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class DrawableComponent implements Drawable {
	private static final String TAG = DrawableComponent.class.getSimpleName();
	
	/**
	 * The gameObject to draw.
	 */
	GameObject owner;
	/**
	 * The collection of bitmaps for this component.
	 */
	private Bitmap bitmaps[];
	/**
	 * The bitmap to draw.
	 */
	private int activeBitmapIndex;

	/**
	 * @param owner
	 * @param bitmaps
	 * @param activeBitmapIndex
	 */
	public DrawableComponent(GameObject owner, Bitmap[] bitmaps,
			int activeBitmapIndex) {
		super();
		this.owner = owner;
		this.bitmaps = bitmaps;
		this.activeBitmapIndex = activeBitmapIndex;
	}
	
	/**
	 * @param owner
	 * @param bitmaps
	 */
	public DrawableComponent(GameObject owner, Bitmap[] bitmaps) {
		super();
		this.owner = owner;
		this.bitmaps = bitmaps;
		this.activeBitmapIndex = 0;
	}

	public void setActiveBitmapIndex(int activeBitmapIndex) {
		this.activeBitmapIndex = activeBitmapIndex;
	}

	@Override
	public void draw(Canvas canvas) {
		Bitmap bitmap = bitmaps[activeBitmapIndex];
		float x = owner.getX() * bitmap.getWidth();
		float y = owner.getY() * bitmap.getHeight();
		canvas.drawBitmap(bitmap, x, y, null);
	}

	@Override
	public void scale(int newWidth, int newHeight) {
		for (int i = 0; i < bitmaps.length; ++i) {
			bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i], newWidth, newHeight, false);
			Log.d(TAG, "Scaled" + i + " dsrW: " + newWidth + " realW: " + bitmaps[i].getWidth());
			Log.d(TAG, "Scaled" + i + " dsrH: " + newHeight + " realH: " + bitmaps[i].getHeight());
		}
	}

}
