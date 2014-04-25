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

	public void draw(Canvas canvas, int[] actRange) {
		Bitmap bitmap = getBitmaps()[Bomb.EXPLODING];
		int bx = bitmap.getWidth();
		int by = bitmap.getHeight();
		float x = owner.getX() * bx;
		float y = owner.getY() * by;
		canvas.drawBitmap(bitmap, x, y, null);

		for (int i = actRange[Bomb.RANGE_LEFT]; i <= actRange[Bomb.RANGE_RIGHT]; ++i) {
			canvas.drawBitmap(bitmap, i * bx, y, null);
		}

		for (int i = actRange[Bomb.RANGE_UP]; i <= actRange[Bomb.RANGE_DOWN]; ++i) {
			canvas.drawBitmap(bitmap, x, i * by, null);
		}
	}

}
