package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

import com.example.bombermancmov.MainGamePanel;
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

	private Level mLevel;
	private boolean isSingleplayer;

	// private Character player;
	private List<Character> mPlayers;
	private List<Droid> mDroids;
	private List<Bomb> mBombs;

	/** Used to choose new directions for the droids. */
	//private DroidAI droidAI;

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
		return mPlayers.get(i);
	}

	public List<Character> getPlayersByPos(float x, float y) {
		float range = 0.01f;
		List<Character> result = new ArrayList<Character>();
		for (Character p : mPlayers) {
			int intX = (int) Math.rint(p.getX());
			int intY = (int) Math.rint(p.getY());
			if (inRange(x, intX, range) && inRange(y, intY, range)) {
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
	public Game(SurfaceView surfaceView, boolean isSingleplayer) {
		super();
		this.gameDuration = 180000; // three minutes?
		this.totalTime = 0;
		
		this.isSingleplayer = isSingleplayer;

		this.mLevel = new Level();
		this.mLevel.setLevelName("default");
		this.mLevel.setExplosionTimeout(4);
		this.mLevel.setExplosionDuration(2000);
		this.mLevel.setExplosionRange(2);
		this.mLevel.setRobotSpeed(1);
		this.mLevel.setPointsPerOpponentKilled(2);
		this.mLevel.setPointsPerRobotKilled(1);
		this.mLevel.setMaxNumberPlayers(3);

		this.surfaceView = surfaceView;

		this.explosionSoundComponent = new SoundComponent(surfaceView);

		decodeResources();

		// Create Lists for filling
		this.mBombs = new ArrayList<Bomb>();
		this.mDroids = new ArrayList<Droid>();
		this.mPlayers = new ArrayList<Character>();

		// Create droidAI
		//droidAI = new DroidAI(mDroids);

		// Creating Player and Robots on their starting position (maybe rework
		// later)
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 19; j++) {
				// Check if a player is on this cell
				if (this.mLevel.getGrid().getGridCell(j, i) == '1'
						&& this.mPlayers.size() < this.mLevel
								.getMaxNumberPlayers()) {
					mPlayers.add(new Character(playerBitmap[PLAYER_1], j, i,
							PLAYER_SPEED, getLevel().getGrid(), this, true));
				} else 
				{
					if(this.isSingleplayer == false) 
					{
						if (this.mLevel.getGrid().getGridCell(j, i) == '2' && this.mPlayers.size() < this.mLevel.getMaxNumberPlayers()) {
							mPlayers.add(new Character(playerBitmap[PLAYER_2], j, i, PLAYER_SPEED, getLevel().getGrid(), this, true));
						} else if (this.mLevel.getGrid().getGridCell(j, i) == '3'
							&& this.mPlayers.size() < this.mLevel
									.getMaxNumberPlayers()) {
						mPlayers.add(new Character(playerBitmap[PLAYER_3], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
						}
					}
				}
					
				if (this.mLevel.getGrid().getGridCell(j, i) == 'R') {
					this.mDroids.add(new Droid(droidBitmap, j, i, this.mLevel
							.getRobotSpeed(), this, true));
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

	public void draw(Canvas canvas) {
		for (int rowNum = 0; rowNum < this.mLevel.getGrid().getRowSize(); ++rowNum) {
			for (int collNum = 0; collNum < this.mLevel.getGrid().getColSize(); ++collNum) {
				switch (this.mLevel.getGrid().getGridCell(rowNum, collNum)) {
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

		for (Character c : mPlayers) {
			if(c.isAlive()) {
				c.draw(canvas);
			}
		}
		for (Bomb b : mBombs) {
			b.draw(canvas);
		}
		for (Character c : mDroids) {
			c.draw(canvas);
		}
	}

	/**
	 * Scale bitmaps only once to increase performance.
	 */
	public void scaleResources() {
		int newWidth = surfaceView.getWidth()
				/ this.mLevel.getGrid().getRowSize();
		int newHeight = surfaceView.getHeight()
				/ this.mLevel.getGrid().getColSize();
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
		for (Bitmap[] bitmaps : playerBitmap) {
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
		mBombs.add(new Bomb(bombBitmap, x, y,
				this.mLevel.getExplosionTimeout(), this.mLevel
						.getExplosionRange(), this.mLevel.getGrid(),
				explosionSoundComponent));
	}

	public boolean nextRound() {
		if (totalTime == gameDuration) {
			return false;
		} else {
			this.gameDuration = this.gameDuration - MainGamePanel.ROUND_TIME;
		}
		
		if(this.isSingleplayer && !mPlayers.get(0).isAlive()) {
			return false;
		}
		
		float t;
		int[] expBlocks;
		List<Bomb> bombsToRemove = new ArrayList<Bomb>();
		for (Bomb b : mBombs) {
			t = b.tick();
			if (t == 0) {
				expBlocks = b.explode(surfaceView.getContext());
				explosionCollision(b, expBlocks);
			} else if (t == -1) {
				bombsToRemove.add(b);
			}
		}
		mBombs.removeAll(bombsToRemove);
		return true;
	}

	/**
	 * Remove all droids in act range and adds to player's points.
	 * 
	 * @param bomb
	 * @param actRange
	 */
	public void explosionCollision(Bomb bomb, int[] actRange) {
		List<Droid> droidsToRemove = new ArrayList<Droid>();
		for (Droid d : mDroids) {
			boolean collidedHorizontally = ((Math.rint(bomb.getY()) == Math
					.rint(d.getY()))
					&& (actRange[Bomb.RANGE_LEFT] <= Math.rint(d.getX())) && (actRange[Bomb.RANGE_RIGHT] >= Math
					.rint(d.getX())));
			boolean collidedVertically = ((Math.rint(bomb.getX()) == Math
					.rint(d.getX()))
					&& (actRange[Bomb.RANGE_UP] <= Math.rint(d.getY())) && (actRange[Bomb.RANGE_DOWN] >= Math
					.rint(d.getY())));
			if (collidedHorizontally || collidedVertically) {
				droidsToRemove.add(d);
			}
		}
		int droidsKilled = droidsToRemove.size();
		mPlayers.get(0).setPoints(
				mPlayers.get(0).getPoints() + droidsKilled
						* mLevel.getPointsPerRobotKilled());
		mDroids.removeAll(droidsToRemove);
	}

	public void update(long timePassed) {
		for (Character p : mPlayers) {
			p.update(timePassed);
		}

		for (Droid d : mDroids) {
			d.updateDroid(timePassed);
			d.update(timePassed);
		}
	}

	/**
	 * GETTERS & SETTERS
	 */
	public Level getLevel() {
		return mLevel;
	}

	public void setLevel(Level level) {
		this.mLevel = level;
	}

	public List<Character> getPlayers() {
		return mPlayers;
	}

	public void setPlayers(List<Character> players) {
		this.mPlayers = players;
	}
}
