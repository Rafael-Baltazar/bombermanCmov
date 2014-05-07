package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import com.example.bombermancmov.R;

public class Game {
	
	private float gameDuration;
	private float totalTime;
	
	private Level level;

	//private Character player;
	private List<Character> players;

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

	/*public Character getPlayer() {
		return player;
	}*/
	
	public Character getPlayerByNumber(int i) {
		return players.get(i);
	}

	public float getTimeLeft() {
		return gameDuration - totalTime;
	}

	public Bitmap getWallBitMap() {
		return wallBitMap;
	}

	/**
	 * To test.
	 */
	public Game(SurfaceView surfaceView) {
		super();
		this.gameDuration = 180000; // three minutes?
		this.totalTime = 0;
		
		this.level = new Level();
		this.level.setLevelName("default");
		this.level.setExplosionTimeout(4);
		this.level.setExplosionDuration(2000);
		this.level.setExplosionRange(2);
		this.level.setRobotSpeed(1);
		this.level.setPointsPerOpponentKilled(2);
		this.level.setPointsPerRobotKilled(1);
		this.level.setMaxNumberPlayers(1);		
		
		this.surfaceView = surfaceView;
		
		decodeResources();
		
		//Create Lists for filling
		this.bombs = new ArrayList<Bomb>();
		this.droids = new ArrayList<Droid>();
		this.players = new ArrayList<Character>();
		
		//Creating Player and Robots on their starting position (maybe rework later)
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 19; j++) {
				//Check if a player is on this cell
				if(this.level.getGrid().getGridCell(i, j) == '1' || this.level.getGrid().getGridCell(i, j) == '2' ||
						this.level.getGrid().getGridCell(i, j) == '3') {
					//Check if additional player is possible for this map
					if(this.players.size() <= this.level.getMaxNumberPlayers()) {
						players.add(new Character(playerBitmap, j, i, 25.0f, this.level.getGrid(), surfaceView));
					}					
					//player = new Character(playerBitmap, j, i, 25.0f, this.level.getGrid(), surfaceView);				
				}
				if(this.level.getGrid().getGridCell(i, j) == 'R') {
					this.droids.add(new Droid(droidBitmap, j, i, this.level.getRobotSpeed(), this.level.getGrid(), surfaceView));
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
		for (int rowNum = 0; rowNum < this.level.getGrid().getRowSize(); ++rowNum) {
			for (int collNum = 0; collNum < this.level.getGrid().getColSize(); ++collNum) {
				switch (this.level.getGrid().getGridCell(collNum, rowNum)) {
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
		
		for (Character c : players) {
			c.draw(canvas);
		}
		//player.draw(canvas);
		for (Bomb b : bombs) {
			b.draw(canvas);
		}
		for (Character c : droids) {
			c.draw(canvas);
		}
	}

	/**
	 * Scale bitmaps only once to increase performance.
	 */
	public void scaleResources() {
		int newWidth = surfaceView.getWidth() / this.level.getGrid().getRowSize();
		int newHeight = surfaceView.getHeight() / this.level.getGrid().getColSize();
		scaleWallBitmap(newWidth, newHeight);
		scaleObstacleBitmap(newWidth, newHeight);
		scaleBombBitmaps(newWidth, newHeight);
		scalePlayerBitmaps(newWidth, newHeight);
		scaleDroidBitmaps(newWidth, newHeight);
	}

	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources()
	 */
	private void scaleWallBitmap(int newWidth, int newHeight) {
		wallBitMap = Bitmap.createScaledBitmap(wallBitMap, newWidth, newHeight,
				false);
	}
	
	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources()
	 */
	private void scaleObstacleBitmap(int newWidth, int newHeight) {
		obstacleBitmap = Bitmap.createScaledBitmap(obstacleBitmap, newWidth,
				newHeight, false);
	}
	
	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources()
	 */
	private void scaleBombBitmaps(int newWidth, int newHeight) {
		scaleBitmapArray(newWidth, newHeight, bombBitmap);
	}
	
	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources()
	 */
	private void scalePlayerBitmaps(int newWidth, int newHeight) {
		scaleBitmapArray(newWidth, newHeight, playerBitmap);
	}
	
	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources()
	 */
	private void scaleDroidBitmaps(int newWidth, int newHeight) {
		scaleBitmapArray(newWidth, newHeight, droidBitmap);
	}

	/**
	 * Scales all given bitmaps to the new width and height.
	 * @param newWidth The new desired width.
	 * @param newHeight The new desired height.
	 * @param bitmaps The bitmap array to be scaled.
	 */
	public void scaleBitmapArray(int newWidth, int newHeight, Bitmap[] bitmaps) {
		for (int i = 0; i < bitmaps.length; ++i) {
			bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i], newWidth, newHeight, false);
		}
	}
	
	public void placeBomb(int x, int y) {
		bombs.add(new Bomb(bombBitmap, x, y, this.level.getExplosionTimeout(), this.level.getExplosionRange(),
				this.level.getGrid()));
	}

	public boolean nextRound() {
		if (totalTime == gameDuration) {
			return false;
		}
		
		Log.d("ROUND", "Num bombs:" + bombs.isEmpty());

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
						//TODO which player? this is for single player:
						players.get(0).setPoints(players.get(0).getSpeed() + this.level.getPointsPerRobotKilled());
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
	
	/**
	 * GETTERS &
	 * SETTERS
	 */
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public List<Character> getPlayers() {
		return players;
	}

	public void setPlayers(List<Character> players) {
		this.players = players;
	}
}
