package com.example.bombermancmov.model.component;

import com.example.bombermancmov.model.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

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
	
	public Bitmap[] getBitmaps() {
		return bitmaps;
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

}
