package com.example.bombermancmov.model;

import com.example.bombermancmov.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

public class Bomb extends GameObject {

	private SurfaceView surfaceView;
	private LevelGrid level;
	private int time;
	private int range;
	private boolean isExploding;

	
	
	public int getRange() {
		return range;
	}

	public Bomb(float x, float y, int time, int range, LevelGrid level, SurfaceView surfaceView) {
		super(null, x, y);
		Bitmap bitmap = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.bomb_0);
		this.setBitmap(bitmap);
		this.time = time;
		this.level = level;
		this.range = range;
		this.isExploding = false;
		this.surfaceView = surfaceView;
	}

	public void draw(Canvas canvas){
		Bitmap map;
		if(isExploding){
			map = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.bomb_2);
			Bitmap expMap = Bitmap.createScaledBitmap(map, getBitmap().getWidth(), getBitmap().getHeight(), false);
			float i;
			for(i= getX()-range; i<=getX()+range; ++i){
				if(i>0 && 
						i<level.getRowSize() && 
						(level.getGridCell((int) Math.rint(i), (int) Math.rint(getY())) == LevelGrid.EMPTY))
				{

					Log.d("BOMB", "Draw explosion in X: " + i + " Y: " + getY());
					canvas.drawBitmap(expMap, i*getBitmap().getWidth(), getY()*getBitmap().getHeight(), null);
				}

			}
			for(i= getY()-range; i<=getY()+range; ++i){
				if(i>0 && 
						i<level.getCollSize() && 
						(level.getGridCell((int) Math.rint(i), (int) Math.rint(getY())) == LevelGrid.EMPTY))
				{
					Log.d("BOMB", "Draw explosion in X: " + getX() + " Y: " + i);
					canvas.drawBitmap(expMap, getX()*getBitmap().getWidth(), i*getBitmap().getHeight(), null);
				}
			}
			
		}else {
			map = getBitmap();
			canvas.drawBitmap(map, getX()*map.getWidth(), getY()*map.getHeight(), null);
		}

	}
	
	public void scale() {
		int newWidth = surfaceView.getWidth() / level.getRowSize();
		int newHeight = surfaceView.getHeight() / level.getCollSize();
		this.setBitmap(Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, false));
		Log.d("SCALE", "ScaledF width: " + newWidth + " real: " + this.getBitmap().getWidth() + 
				" ScaledF height: " + newHeight + " real: " + this.getBitmap().getHeight());
	}

	public int tick(){
		return time--;
	}
	
	
	public void explode(){
		isExploding = true;
		Log.d("BOMB", "Bomb exploded in X: " + this.getX() + " Y: " + this.getY());
	}

}

