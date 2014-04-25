package com.example.bombermancmov.model.component;

import android.graphics.Canvas;

public interface Drawable {
	public void draw(Canvas canvas);
	public void scale(int newWidth, int newHeight);
}
