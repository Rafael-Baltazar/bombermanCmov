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
	private int numberPlayers;
	private LevelGrid grid = new LevelGrid();

	private Character player;
	private List<Droid> droids;
	private List<Bomb> bombs;

	/* Drawing */
	/* Load bitmaps only once to increase performance */
	private Bitmap wallBitMap;
	private Bitmap obstacleBitmap;
	private Bitmap[] bombBitmap;
	private Bitmap[] playerBitmap;
	private Bitmap[] droidBitmap;

	private SurfaceView surfaceView;

	public Character getPlayer() {
		return player;
	}

	public int getNumberPlayers() {
		return numberPlayers;
	}

	public float getTimeLeft() {
		return gameDuration - totalTime;
	}

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
		this.explosionTimeout = 4;
		this.explosionDuration = 2000;
		this.explosionRange = 2;
		this.robotSpeed = 1; // 1 cell per second
		this.pointsPerRobotKilled = 1;
		this.pointsPerOpponentKilled = 2;
		this.numberPlayers = 1; // so far
		this.surfaceView = surfaceView;
		
		decodeResources();
		
		this.bombs = new ArrayList<Bomb>();
		this.droids = new ArrayList<Droid>();
		
		//awful dynamic position getting for player & droid
		char [][] gamefield = new char [][] {
				{ 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W' },
				{ 'W', '-', '-', '-', '-', '1', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'O', 'O', 'W' },
				{ 'W', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
				{ 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
				{ 'W', 'W', '-', 'W', 'O', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
				{ 'W', '-', '-', '-', 'O', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
				{ 'W', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
				{ 'W', '-', 'R', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
				{ 'W', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
				{ 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
				{ 'W', 'W', '-', 'W', 'O', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
				{ 'W', '-', '-', '-', 'O', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
				{ 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W' } 
				};		
		this.grid.setGridLayout(gamefield, 19, 13);
		
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 19; j++) {
				if(gamefield[i][j] == '1') {
					player = new Character(playerBitmap, j, i, 25.0f, grid, surfaceView);				
				}
				if(gamefield[i][j] == 'R') {
					this.droids.add(new Droid(droidBitmap, j, i, robotSpeed, grid, surfaceView));
				}
			}
		}
	}

	/**
	 * Load resources only once to increase performance.
	 */
	private void decodeResources() {
		decodeWallBitmap();
		decodeObstacleBitmap();
		decodeBombBitmaps();
		decodePlayerBitmaps();
		decodeDroidBitmaps();
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeWallBitmap() {
		wallBitMap = BitmapFactory.decodeResource(surfaceView.getResources(),
				R.drawable.wall_1);
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeObstacleBitmap() {
		obstacleBitmap = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.obstacle_0);
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeBombBitmaps() {
		bombBitmap = new Bitmap[3];
		bombBitmap[Bomb.NORMAL] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.bomb_0);
		bombBitmap[Bomb.NEARLY] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.bomb_1);
		bombBitmap[Bomb.EXPLODING] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.bomb_2);		
	}
	
	/**
	 * @see #decodeResources()
	 */
	private void decodePlayerBitmaps() {
		playerBitmap = new Bitmap[4];
		playerBitmap[Character.FRONT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_0);
		playerBitmap[Character.LEFT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_1);
		playerBitmap[Character.RIGHT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_2);
		playerBitmap[Character.BACK] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_3);
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeDroidBitmaps() {
		droidBitmap = new Bitmap[4];
		droidBitmap[Character.FRONT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_0);
		droidBitmap[Character.LEFT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_1);
		droidBitmap[Character.RIGHT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_2);
		droidBitmap[Character.BACK] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.c0_3);
	}

	public void draw(Canvas canvas) {
		for (int rowNum = 0; rowNum < grid.getRowSize(); ++rowNum) {
			for (int collNum = 0; collNum < grid.getCollSize(); ++collNum) {
				switch (grid.getGridCell(collNum, rowNum)) {
				case LevelGrid.WALL: {
					canvas.drawBitmap(wallBitMap,
							rowNum * wallBitMap.getWidth(), collNum
									* wallBitMap.getHeight(), null);
					break;
				}
				case LevelGrid.OBSTACLE: {
					canvas.drawBitmap(obstacleBitmap,
							rowNum * obstacleBitmap.getWidth(), collNum
									* obstacleBitmap.getHeight(), null);
					break;
				}
				}
			}
		}
		
		player.draw(canvas);
		for (Bomb b : bombs) {
			b.draw(canvas);
		}
		for (Character c : droids) {
			c.draw(canvas);
		}
	}

	public void scale() {
		int newWidth = surfaceView.getWidth() / grid.getRowSize();
		int newHeight = surfaceView.getHeight() / grid.getCollSize();
		wallBitMap = Bitmap.createScaledBitmap(wallBitMap, newWidth, newHeight,
				false);
		obstacleBitmap = Bitmap.createScaledBitmap(obstacleBitmap, newWidth,
				newHeight, false);

		player.scale();

		for (Bomb b : bombs) {
			b.scale();
		}

		for (Character c : droids) {
			c.scale();
		}

		Log.d("SCALE", "Scaled Wall width: " + newWidth + " real: "
				+ wallBitMap.getWidth() + " Scaled Level height: " + newHeight
				+ " real: " + wallBitMap.getHeight());
	}

	public void placeBomb(int x, int y) {
		bombs.add(new Bomb(bombBitmap, x, y, explosionTimeout, explosionRange,
				this.grid, surfaceView));
	}

	public boolean nextRound() {
		if (totalTime == gameDuration) {
			return false;
		}
		
		Log.d("ROUND", "Num bombs:" + bombs.isEmpty());

		Random r;
		float t;
		int[] expBlocks;
		List<Bomb> toRemove = new ArrayList<Bomb>(); // STUPID HACK TO PREVENT
														// MULTI-BOMB CRASH
		for (Bomb b : bombs) {
			t = b.tick();
			if (t == 0) {
				expBlocks = b.explode(surfaceView.getContext());
				for (Character c : droids) {
					if (((Math.rint(b.getY()) == Math.rint(c.getY())) && (expBlocks[2] <= Math.rint(c.getX())) && (expBlocks[3] >= Math.rint(c.getX())))
							|| 
						((Math.rint(b.getX()) == Math.rint(c.getX())) && (expBlocks[0] <= Math.rint(c.getY())) && (expBlocks[1] >= Math.rint(c.getY())))) {
						
						droids.remove(c);
						player.setPoints(player.getSpeed()
								+ pointsPerRobotKilled);
					}
				}
			} else if (t == -1) {
				toRemove.add(b);
			}
		}
		
		for (Bomb b : toRemove) {
			bombs.remove(b);
		}

		for (Droid d : droids) {
			d.moveRandomly();
		}
		
		return true;
	}

}
