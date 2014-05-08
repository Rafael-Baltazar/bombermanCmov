package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import com.example.bombermancmov.R;
import com.example.bombermancmov.model.component.SoundComponent;

public class Game {

	// Used for bitmap purposes
	private static final int PLAYER_1 = 0;
	private static final int PLAYER_2 = 1;
	private static final int PLAYER_3 = 2;
	private static final float PLAYER_SPEED = 3.0f;
	
	private float gameDuration;
	private float totalTime;

	private Level level;

	// private Character player;
	private List<Character> players;
	private List<Droid> droids;
	private List<Bomb> bombs;
	
	// dead players & droids
	private List<Character> deadPlayers;
	private List<Droid> deadDroids;
	private List<Bomb> unusedBombs;

	/** Used to choose new directions for the droids. */
	private DroidAI droidAI;

	/* Drawing */
	/* Load bitmaps only once to increase performance */
	private Bitmap wallBitMap;
	private Bitmap obstacleBitmap;
	private Bitmap[] bombBitmap;
	private Bitmap[][] playerBitmap;
	private Bitmap[] droidBitmap;

	private SurfaceView surfaceView;
	private SoundComponent explosionSoundComponent;

	/*
	 * public Character getPlayer() { return player; }
	 */

	public Character getPlayerByNumber(int i) {
		return players.get(i);
	}

	public List<Character> getPlayersByPos(float x, float y) {
		float range = 0.01f;
		List<Character> result = new ArrayList<Character>();
		for(Character p : players) {
			int intX = (int) Math.rint(p.getX());
			int intY = (int) Math.rint(p.getY());
			if(inRange(x, intX, range) && inRange(y, intY, range)) {
				result.add(p);
			}
		}
		return result;
	}

	private boolean inRange(float a, float b, float range) {
		return (a - range <= b && b <= a) || (a <= b && b <= a + range);
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
		this.level.setMaxNumberPlayers(3);

		this.surfaceView = surfaceView;
		
		this.explosionSoundComponent = new SoundComponent(surfaceView);

		decodeResources();

		// Create Lists for filling
		this.bombs = new ArrayList<Bomb>();
		this.droids = new ArrayList<Droid>();
		this.players = new ArrayList<Character>();

		// Create droidAI
		droidAI = new DroidAI(droids);

		// Creating Player and Robots on their starting position (maybe rework
		// later)
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 19; j++) {
				// Check if a player is on this cell
				if (this.level.getGrid().getGridCell(j, i) == '1' && this.players.size() < this.level.getMaxNumberPlayers()) {
					players.add(new Character(playerBitmap[PLAYER_1], j, i,
							PLAYER_SPEED, getLevel().getGrid(), this));
				} else if(this.level.getGrid().getGridCell(j, i) == '2' && this.players.size() < this.level.getMaxNumberPlayers()) {
					players.add(new Character(playerBitmap[PLAYER_2], j, i,
							PLAYER_SPEED, getLevel().getGrid(), this));
				} else if(this.level.getGrid().getGridCell(j, i) == '3' && this.players.size() < this.level.getMaxNumberPlayers()) {
					players.add(new Character(playerBitmap[PLAYER_3], j, i,
							PLAYER_SPEED, getLevel().getGrid(), this));
				}
				if (this.level.getGrid().getGridCell(j, i) == 'R') {
					this.droids.add(new Droid(droidBitmap, j, i, this.level
							.getRobotSpeed(), this));
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
		decodeExplosionSound();
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
		playerBitmap = new Bitmap[3][4];
		decodePlayer1Bitmaps();
		decodePlayer2Bitmaps();
		decodeAndRetPlayer3Bitmaps();
	}
	
	private void decodePlayer1Bitmaps() {
		playerBitmap[PLAYER_1][Character.FRONT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.eevee_front);
		playerBitmap[PLAYER_1][Character.LEFT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.eevee_left);
		playerBitmap[PLAYER_1][Character.RIGHT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.eevee_right);
		playerBitmap[PLAYER_1][Character.BACK] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.eevee_back);
	}
	
	private void decodePlayer2Bitmaps() {
		playerBitmap[PLAYER_2][Character.FRONT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.espeon_front);
		playerBitmap[PLAYER_2][Character.LEFT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.espeon_left);
		playerBitmap[PLAYER_2][Character.RIGHT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.espeon_right);
		playerBitmap[PLAYER_2][Character.BACK] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.espeon_back);
	}
	
	private void decodeAndRetPlayer3Bitmaps() {
		playerBitmap[PLAYER_3][Character.FRONT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.flareon_front);
		playerBitmap[PLAYER_3][Character.LEFT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.flareon_left);
		playerBitmap[PLAYER_3][Character.RIGHT] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.flareon_right);
		playerBitmap[PLAYER_3][Character.BACK] = BitmapFactory.decodeResource(
				surfaceView.getResources(), R.drawable.flareon_back);
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
	
	/**
	 * @see #decodeResources()
	 */
	private void decodeExplosionSound() {
	}

	public void draw(Canvas canvas) {
		for (int rowNum = 0; rowNum < this.level.getGrid().getRowSize(); ++rowNum) {
			for (int collNum = 0; collNum < this.level.getGrid().getColSize(); ++collNum) {
				switch (this.level.getGrid().getGridCell(rowNum, collNum)) {
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
		int newWidth = surfaceView.getWidth()
				/ this.level.getGrid().getRowSize();
		int newHeight = surfaceView.getHeight()
				/ this.level.getGrid().getColSize();
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
		for(Bitmap[] bitmaps : playerBitmap) {
			scaleBitmapArray(newWidth, newHeight, bitmaps);
		}
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
	 * 
	 * @param newWidth
	 *            The new desired width.
	 * @param newHeight
	 *            The new desired height.
	 * @param bitmaps
	 *            The bitmap array to be scaled.
	 */
	private void scaleBitmapArray(int newWidth, int newHeight, Bitmap[] bitmaps) {
		for (int i = 0; i < bitmaps.length; ++i) {
			bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i], newWidth,
					newHeight, false);
		}
	}

	public void placeBomb(int x, int y) {
		bombs.add(new Bomb(bombBitmap, x, y, this.level.getExplosionTimeout(),
				this.level.getExplosionRange(), this.level.getGrid(), explosionSoundComponent));
	}

	public boolean nextRound() {
		if (totalTime == gameDuration) {
			return false;
		}
		float t;
		int[] expBlocks;
		List<Bomb> toRemove = new ArrayList<Bomb>(); // STUPID HACK TO PREVENT
														// MULTI-BOMB CRASH
		for (Bomb b : bombs) {
			t = b.tick();
			if (t == 0) {
				expBlocks = b.explode(surfaceView.getContext());
				for (Character c : droids) {
					if (((Math.rint(b.getY()) == Math.rint(c.getY()))
							&& (expBlocks[2] <= Math.rint(c.getX())) && (expBlocks[3] >= Math
							.rint(c.getX())))
							|| ((Math.rint(b.getX()) == Math.rint(c.getX()))
									&& (expBlocks[0] <= Math.rint(c.getY())) && (expBlocks[1] >= Math
									.rint(c.getY())))) {

						droids.remove(c);
						// TODO which player? this is for single player:
						players.get(0).setPoints(
								players.get(0).getSpeed()
										+ this.level.getPointsPerRobotKilled());
					}
				}
			} else if (t == -1) {
				toRemove.add(b);
			}
		}

		for (Bomb b : toRemove) {
			bombs.remove(b);
		}

		// for (Droid d : droids) {
		// d.moveRandomly();
		// }

		return true;
	}

	public void update(long timePassed) {
		for (Character p : players) {
			p.update(timePassed);
		}
		droidAI.update(timePassed);
		for (Droid d : droids) {
			d.update(timePassed);
		}
	}

	/**
	 * GETTERS & SETTERS
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
