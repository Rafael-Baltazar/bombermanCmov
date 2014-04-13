package com.example.bombermancmov.model;

import com.example.bombermancmov.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

public class Level {
	private String levelName;
	private float gameDuration;
	private float explosionTimeout;
	private float explosionDuration;
	private float explosionRange;
	private float robotSpeed;
	private float pointsPerRobotKilled;
	private float pointsPerOpponentKilled;
	private LevelGrid grid = new LevelGrid();
	
	/* Drawing */
	private Bitmap wallBitMap;
	private SurfaceView surfaceView;
	
	public LevelGrid getGrid() {
		return grid;
	}
	public void setGrid(LevelGrid grid) {
		this.grid = grid;
	}

	/**
	 * To test.
	 */
	public Level(SurfaceView surfaceView) {
		super();
		this.levelName = "defaultLevelName";
		this.gameDuration = 180000; // three minutes?
		this.explosionTimeout = 1500;
		this.explosionDuration = 1000;
		this.explosionRange = 1;
		this.robotSpeed = 1; // 1 cell per second
		this.pointsPerRobotKilled = 1;
		this.pointsPerOpponentKilled = 2;
		this.grid.setGridLayout(new char[][] {{'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
											  {'W', '-', '-', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W'},
											  {'W', '-', '-', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W'},
											  {'W', '-', '-', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W'},
											  {'W', '-', '-', '-', '-', '-', '-', '-', 'W'},
											  {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}}, 9, 9);
		this.surfaceView = surfaceView;
		this.wallBitMap = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.wall_1);
	}
	
	public void draw(Canvas canvas){
		for(int rowNum = 0; rowNum < grid.getRowSize(); ++rowNum){
			for(int collNum = 0; collNum < grid.getCollSize(); ++collNum){
				switch(grid.getGridCell(collNum, rowNum)){
					case 'W':
					{
						canvas.drawBitmap(wallBitMap, rowNum*wallBitMap.getWidth(), collNum*wallBitMap.getHeight(), null);
						break;
					}
				}
			}	
		}
	}
	
	public void scale() {
		int newWidth = surfaceView.getWidth() / grid.getRowSize();
		int newHeight = surfaceView.getHeight() / grid.getCollSize();
		wallBitMap = Bitmap.createScaledBitmap(wallBitMap, newWidth, newHeight, false);
		Log.d("SCALE", "Scaled Level width: " + newWidth + " real: " + wallBitMap.getWidth() + 
				" Scaled Level height: " + newHeight + " real: " + wallBitMap.getHeight());
	}
}
