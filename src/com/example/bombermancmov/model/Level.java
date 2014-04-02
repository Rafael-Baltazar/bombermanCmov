package com.example.bombermancmov.model;

import com.example.bombermancmov.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
	
	private SurfaceView surfaceHolder;
	
	private LevelGrid grid = new LevelGrid();
	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public float getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(float gameDuration) {
		this.gameDuration = gameDuration;
	}

	public float getExplosionTimeout() {
		return explosionTimeout;
	}

	public void setExplosionTimeout(float explosionTimeout) {
		this.explosionTimeout = explosionTimeout;
	}

	public float getExplosionDuration() {
		return explosionDuration;
	}

	public void setExplosionDuration(float explosionDuration) {
		this.explosionDuration = explosionDuration;
	}

	public float getExplosionRange() {
		return explosionRange;
	}

	public void setExplosionRange(float explosionRange) {
		this.explosionRange = explosionRange;
	}

	public float getRobotSpeed() {
		return robotSpeed;
	}

	public void setRobotSpeed(float robotSpeed) {
		this.robotSpeed = robotSpeed;
	}

	public float getPointsPerRobotKilled() {
		return pointsPerRobotKilled;
	}

	public void setPointsPerRobotKilled(float pointsPerRobotKilled) {
		this.pointsPerRobotKilled = pointsPerRobotKilled;
	}

	public float getPointsPerOpponentKilled() {
		return pointsPerOpponentKilled;
	}

	public void setPointsPerOpponentKilled(float pointsPerOpponentKilled) {
		this.pointsPerOpponentKilled = pointsPerOpponentKilled;
	}

	public LevelGrid getGrid() {
		return grid;
	}

	public void setGrid(LevelGrid grid) {
		this.grid = grid;
	}

	/**
	 * @param levelName
	 * @param gameDuration
	 * @param explosionTimeout
	 * @param explosionDuration
	 * @param explosionRange
	 * @param robotSpeed
	 * @param pointsPerRobotKilled
	 * @param pointsPerOpponentKilled
	 * @param gridLayout
	 */
	public Level(String levelName, float gameDuration, float explosionTimeout,
			float explosionDuration, float explosionRange, float robotSpeed,
			float pointsPerRobotKilled, float pointsPerOpponentKilled,
			char[][] gridLayout, int rowSize, int collSize, SurfaceView surfaceHolder) {
		super();
		this.levelName = levelName;
		this.gameDuration = gameDuration;
		this.explosionTimeout = explosionTimeout;
		this.explosionDuration = explosionDuration;
		this.explosionRange = explosionRange;
		this.robotSpeed = robotSpeed;
		this.pointsPerRobotKilled = pointsPerRobotKilled;
		this.pointsPerOpponentKilled = pointsPerOpponentKilled;
		this.grid.setGridLayout(gridLayout, rowSize, collSize);
		this.surfaceHolder = surfaceHolder;
	}

	/**
	 * To test.
	 */
	public Level(SurfaceView surfaceHolder) {
		super();
		this.levelName = "defaultLevelName";
		this.gameDuration = 180000; // three minutes?
		this.explosionTimeout = 1500;
		this.explosionDuration = 1000;
		this.explosionRange = 1;
		this.robotSpeed = 1; // 1 cell per second
		this.pointsPerRobotKilled = 1;
		this.pointsPerOpponentKilled = 2;
		this.grid.setGridLayout(new char[][] {{'W', 'W', 'W', 'W', 'W', 'W', 'W'},
											  {'W', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', 'W', '-', 'W', '-', 'W'},
											  {'W', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', 'W', '-', 'W', '-', 'W'},
											  {'W', 'W', 'W', '-', 'W', 'W', 'W'},
											  {'W', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', '-', '-', '-', '-', 'W'},
											  {'W', '-', '-', '-', '-', '-', 'W'},
											  {'W', 'W', 'W', 'W', 'W', 'W', 'W'}}, 7, 10);
		this.surfaceHolder = surfaceHolder;
	}
	
	public void draw(Canvas canvas){
		int rowNum, collNum;
		Bitmap wallBitMap = BitmapFactory.decodeResource(surfaceHolder.getResources(), R.drawable.wall_1);
		Bitmap droidBitmap = BitmapFactory.decodeResource(surfaceHolder.getResources(), R.drawable.droid_1);
		
		int newWight = surfaceHolder.getWidth() / grid.getRowSize();
		int newHeight = surfaceHolder.getHeight() / grid.getCollSize();
		
		canvas.drawColor(Color.WHITE);
		
		for(rowNum = 0; rowNum < grid.getRowSize(); ++rowNum){
			for(collNum = 0; collNum < grid.getCollSize(); ++collNum){
				switch(grid.getGridCell(collNum, rowNum)){
					case 'W': 
					{
						canvas.drawBitmap(Bitmap.createScaledBitmap(wallBitMap, newWight, newHeight, false), rowNum*newWight, collNum*newHeight , null);
						break;
					}
					case 'R':
					{
						canvas.drawBitmap(droidBitmap,rowNum*droidBitmap.getWidth(), collNum*droidBitmap.getHeight(), null);
						break;
					}
					default :
					{
						break;
					}
				}
			}	
		}
	}
}
