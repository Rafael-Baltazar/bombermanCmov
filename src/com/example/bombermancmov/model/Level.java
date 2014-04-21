package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import com.example.bombermancmov.R;

public class Level {

	private String levelName;
	private float gameDuration;
	private float totalTime;
	private int explosionTimeout;
	private float explosionDuration;
	private float explosionRange;
	private float robotSpeed;
	private float pointsPerRobotKilled;
	private float pointsPerOpponentKilled;
	private LevelGrid grid = new LevelGrid();
	
	private Character player;
	private List<Character> droids;
	private List<Bomb> bombs;
	
	/* Drawing */
	private Bitmap wallBitMap;
	private Bitmap obstacleBitmap;
	private Bitmap[] bombBitmap;
	private SurfaceView surfaceView;
	
	
	
	public LevelGrid getGrid() {
		return grid;
	}
	public void setGrid(LevelGrid grid) {
		this.grid = grid;
	}
	public Bitmap getWallBitMap() {
		return wallBitMap;
	}
	
	/**
	 * To test.
	 */
	public Level(SurfaceView surfaceView) {
		super();
		this.levelName = "defaultLevelName";
		this.gameDuration = 180000; // three minutes?
		this.totalTime = 0;
		this.explosionTimeout = 1500;
		this.explosionDuration = 1000;
		this.explosionRange = 1;
		this.robotSpeed = 1; // 1 cell per second
		this.pointsPerRobotKilled = 1;
		this.pointsPerOpponentKilled = 2;
		this.grid.setGridLayout(new char[][] {{'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'},
											  {'W','-','-','-','-','1','-','-','-','-','-','-','-','-','-','-','O','O','W'},
											  {'W','W','-','W','-','W','-','W','-','W','-','W','-','W','-','W','-','W','W'},
											  {'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
											  {'W','W','-','W','O','W','-','W','-','W','-','W','-','W','-','W','-','W','W'},
											  {'W','-','-','-','O','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
											  {'W','W','-','W','-','W','-','W','-','W','-','W','-','W','-','W','-','W','W'},
											  {'W','-','R','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
											  {'W','W','-','W','-','W','-','W','-','W','-','W','-','W','-','W','-','W','W'},
											  {'W','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
											  {'W','W','-','W','O','W','-','W','-','W','-','W','-','W','-','W','-','W','W'},
											  {'W','-','-','-','O','-','-','-','-','-','-','-','-','-','-','-','-','-','W'},
											  {'W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W','W'}}, 19, 13);
		this.surfaceView = surfaceView;
		this.bombs = new ArrayList<Bomb>();
		this.droids = new ArrayList<Character>(); 
		this.wallBitMap = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.wall_1);
		this.obstacleBitmap = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.obstacle_0);
		this.bombBitmap = new Bitmap[3];
		this.bombBitmap[0] = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.bomb_0); //normal
		this.bombBitmap[1] = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.bomb_1); //nearly exploding
		this.bombBitmap[2] = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.bomb_2); //exploding
		
		
		this.droids.add(new Character(1.0f, 1.0f, 1.0f, grid, surfaceView));
		
	}
	
	public void draw(Canvas canvas){
		for(int rowNum = 0; rowNum < grid.getRowSize(); ++rowNum){
			for(int collNum = 0; collNum < grid.getCollSize(); ++collNum){
				switch(grid.getGridCell(collNum, rowNum)){
					case LevelGrid.WALL:
					{
						canvas.drawBitmap(wallBitMap, rowNum*wallBitMap.getWidth(), collNum*wallBitMap.getHeight(), null);
						break;
					}
					case LevelGrid.OBSTACLE:
					{
						canvas.drawBitmap(obstacleBitmap, rowNum*obstacleBitmap.getWidth(), collNum*obstacleBitmap.getHeight(), null);
						break;
					}
				}
			}	
		}
		for(Bomb b : bombs){
			b.draw(canvas);
		}
		for(Character c : droids){
			c.draw(canvas);
		}
	}
	
	public void scale() {
		int newWidth = surfaceView.getWidth() / grid.getRowSize();
		int newHeight = surfaceView.getHeight() / grid.getCollSize();
		wallBitMap = Bitmap.createScaledBitmap(wallBitMap, newWidth, newHeight, false);
		obstacleBitmap = Bitmap.createScaledBitmap(obstacleBitmap, newWidth, newHeight, false);
		
		for(Bomb b : bombs){
			b.scale();
		}
		
		for(Character c : droids){
			c.scale();
		}
		
		Log.d("SCALE", "Scaled Level width: " + newWidth + " real: " + wallBitMap.getWidth() + 
				" Scaled Level height: " + newHeight + " real: " + wallBitMap.getHeight());
	}
	
	public void placeBomb(float x, float y){
		bombs.add(new Bomb(bombBitmap, x, y, 4, explosionRange, this.grid, surfaceView));
	}
	
	public boolean nextRound(){
		if(totalTime == gameDuration){
			return false;
		}
		Log.d("ROUND", "Num bombs:" + bombs.isEmpty());
		Random r;
		float t;
		int[] expBlocks;
		List<Bomb> toRemove = new ArrayList<Bomb>(); //STUPID HACK TO PREVENT MULTI-BOMB CRASH
		for(Bomb b : bombs){
			t=b.tick();
			if(t==0){
				expBlocks = b.explode(surfaceView.getContext());
				for(Character c : droids){
					if(((b.getY()==c.getY()) && (expBlocks[2] <= c.getX()) &&  
					    (expBlocks[3] >= c.getX())) ||
					   ((b.getX()==c.getX()) && (expBlocks[0] <= c.getY()) &&  
						(expBlocks[1] >= c.getY()))){
						droids.remove(c);
					}
				}
			}else if(t==-1) {
				toRemove.add(b);
			}
		}
		for(Bomb b : toRemove){
			bombs.remove(b);
		}

		for(Character c : droids){
			r = new Random();
			switch(r.nextInt(4)){
				case 0:{
					c.moveUp();
					break;
				}
				case 1:{
					c.moveLeft();
					break;
				}
				case 2:{
					c.moveRight();
					break;
				}
				case 3:{
					c.moveDown();
					break;
				}
			}
		}
		return true;
	}

}
