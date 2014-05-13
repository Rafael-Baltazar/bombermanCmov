package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.bombermancmov.MainGamePanel;

public class Game {
	// Used for bitmap purposes
	public static final int PLAYER_1 = 0;
	public static final int PLAYER_2 = 1;
	public static final int PLAYER_3 = 2;
	private static final float PLAYER_SPEED = 3.0f;

	private float gameDuration;
	private float totalTime;

	private Level mLevel;
	
	private boolean isSingleplayer;
	private boolean finished;
	private int endStatus; //Singleplayer: 0 = not ended, 1 = win, 2 = lost/killed, 3 = lost/timeover

	// private Character player;
	private List<Character> mPlayers;
	private List<Droid> mDroids;
	private List<Bomb> mBombs;

	private Resource mResources;

	public Game(Resource resources, boolean isSingleplayer) {
		super();
		this.gameDuration = 180000; // three minutes?
		this.totalTime = 0;
		
		this.isSingleplayer = isSingleplayer;
		this.finished = false;
		this.endStatus = 0;

		this.mLevel = new Level();
		this.mLevel.setLevelName("default");
		this.mLevel.setExplosionTimeout(4);
		this.mLevel.setExplosionDuration(2000);
		this.mLevel.setExplosionRange(2);
		this.mLevel.setRobotSpeed(1);
		this.mLevel.setPointsPerOpponentKilled(2);
		this.mLevel.setPointsPerRobotKilled(1);
		this.mLevel.setMaxNumberPlayers(3);

		mResources = resources;
		mResources.decodeResources();

		// Create Lists for filling
		this.mBombs = new ArrayList<Bomb>();
		this.mDroids = new ArrayList<Droid>();
		this.mPlayers = new ArrayList<Character>();

		// Creating Player and Robots on their starting position (maybe rework
		// later)
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 19; j++) {
				// Check if a player is on this cell
				if (this.mLevel.getGrid().getGridCell(j, i) == '1'
						&& this.mPlayers.size() < this.mLevel
								.getMaxNumberPlayers()) {
					mPlayers.add(new Character(mResources.getPlayerBitmap()[PLAYER_1], j, i,
							PLAYER_SPEED, getLevel().getGrid(), this, true));
				} else 
				{
					if(this.isSingleplayer == false) 
					{
						if (this.mLevel.getGrid().getGridCell(j, i) == '2' && this.mPlayers.size() < this.mLevel.getMaxNumberPlayers()) {
							mPlayers.add(new Character(mResources.getPlayerBitmap()[PLAYER_2], j, i, PLAYER_SPEED, getLevel().getGrid(), this, true));
						} else if (this.mLevel.getGrid().getGridCell(j, i) == '3'
							&& this.mPlayers.size() < this.mLevel
									.getMaxNumberPlayers()) {
						mPlayers.add(new Character(mResources.getPlayerBitmap()[PLAYER_3], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
						}
					}
				}
					
				if (this.mLevel.getGrid().getGridCell(j, i) == 'R') {
					this.mDroids.add(new Droid(mResources.getDroidBitmap(), j, i, this.mLevel
							.getRobotSpeed(), this, true));
				}
			}
		}
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
	
	public void placeBomb(int x, int y) {
		mBombs.add(new Bomb(mResources.getBombBitmap(), x, y,
				this.mLevel.getExplosionTimeout(), this.mLevel
						.getExplosionRange(), this.mLevel.getGrid(),
				mResources.getExplosionSoundComponent()));
	}

	public boolean nextRound() {
		this.gameDuration = this.gameDuration - MainGamePanel.ROUND_TIME; 
		
		float t;
		int[] expBlocks;
		List<Bomb> bombsToRemove = new ArrayList<Bomb>();
		for (Bomb b : mBombs) {
			t = b.tick();
			if (t == 0) {
				expBlocks = b.explode(mResources.getSurfaceView().getContext());
				explosionCollision(b, expBlocks);
			} else if (t == -1) {
				bombsToRemove.add(b);
			}
		}
		mBombs.removeAll(bombsToRemove);
		
		//RETURN false, if time is over || singleplayer: player died or no more droids 
		if (totalTime == gameDuration || (this.isSingleplayer && (!mPlayers.get(0).isAlive()) || this.mDroids.size() == 0)) {
			this.finished = true;
			
			if(this.mDroids.size() == 0 && this.mPlayers.get(0).isAlive()) {
				this.endStatus = 1;
			}		
			
			if(!this.mPlayers.get(0).isAlive()) {
				this.endStatus = 2;
			}
			
			if(this.totalTime == gameDuration) {
				this.endStatus = 3;
			}
			
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Remove all droids in act range and adds to player's points.
	 * 
	 * @param bomb
	 * @param actRange
	 */
	public void explosionCollision(Bomb bomb, int[] actRange) {
		Iterator<Droid> it = mDroids.iterator();
		Droid d = null;
		
		while(it.hasNext()) {
			d = it.next();			
			//check on collision
			boolean collidedHorizontally = ((Math.rint(bomb.getY()) == Math
					.rint(d.getY()))
					&& (actRange[Bomb.RANGE_LEFT] <= Math.rint(d.getX())) && (actRange[Bomb.RANGE_RIGHT] >= Math
					.rint(d.getX())));
			boolean collidedVertically = ((Math.rint(bomb.getX()) == Math
					.rint(d.getX()))
					&& (actRange[Bomb.RANGE_UP] <= Math.rint(d.getY())) && (actRange[Bomb.RANGE_DOWN] >= Math
					.rint(d.getY())));
			
			if (collidedHorizontally || collidedVertically) {
				it.remove();
				if(this.isSingleplayer) {
					mPlayers.get(0).setPoints(mPlayers.get(0).getPoints() + mLevel.getPointsPerRobotKilled());
				}
			}
		}		
	}

	public void update(long timePassed) {
		for (Character p : mPlayers) {
			if(p.isAlive()) {
				p.update(timePassed);
			}
		}

		for (Droid d : mDroids) {
			d.updateDroid(timePassed);
			d.update(timePassed);
		}
	}

	/**
	 * DRAWING METHODS
	 */
	public void draw(Canvas canvas) {
		for (int rowNum = 0; rowNum < this.mLevel.getGrid().getRowSize(); ++rowNum) {
			for (int collNum = 0; collNum < this.mLevel.getGrid().getColSize(); ++collNum) {
				switch (this.mLevel.getGrid().getGridCell(rowNum, collNum)) {
				case LevelGrid.WALL: {
					canvas.drawBitmap(mResources.getWallBitMap(),
							rowNum * mResources.getWallBitMap().getWidth(), collNum
									* mResources.getWallBitMap().getHeight(), null);
					break;
				}
				case LevelGrid.OBSTACLE: {
					canvas.drawBitmap(mResources.getObstacleBitmap(),
							rowNum * mResources.getObstacleBitmap().getWidth(), collNum
									* mResources.getObstacleBitmap().getHeight(), null);
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
	 * GETTERS & SETTERS
	 */
	public float getTimeLeft() {
		return gameDuration - totalTime;
	}

	public Bitmap getWallBitMap() {
		return mResources.getWallBitMap();
	}
	
	public Character getPlayerByNumber(int i) {
		return mPlayers.get(i);
	}
	
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

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}	

	public int getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(int endStatus) {
		this.endStatus = endStatus;
	}
}
