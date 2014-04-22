package com.example.bombermancmov.model.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.bombermancmov.model.Bomb;
import com.example.bombermancmov.model.GameObject;

public class DrawableExplosionComponent extends DrawableComponent {

	public DrawableExplosionComponent(GameObject owner, Bitmap[] bitmaps,
			int activeBitmapIndex) {
		super(owner, bitmaps, activeBitmapIndex);
	}

	public void draw(Canvas canvas, float range) {
		Bitmap bitmap = getBitmaps()[Bomb.EXPLODING];
		int bx = bitmap.getWidth();
		int by = bitmap.getHeight();
		float x = owner.getX() * bx;
		float y = owner.getY() * by;
		canvas.drawBitmap(bitmap, x, y, null);

		for (int i = 0; i < range; ++i) {
			canvas.drawBitmap(bitmap, x + (i + 1) * bx, y, null);
			canvas.drawBitmap(bitmap, x - (i + 1) * bx, y, null);
			canvas.drawBitmap(bitmap, x, y + (i + 1) * by, null);
			canvas.drawBitmap(bitmap, x, y - (i + 1) * by, null);
		}
	}

}
