package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.bombermancmov.LocalPlayerInput;
import com.example.bombermancmov.PlayerInput;
import com.example.bombermancmov.RemotePlayerInput;

public class Game {
	// Used for bitmap purposes
	public static final int PLAYER_1 = 0;
	public static final int PLAYER_2 = 1;
	public static final int PLAYER_3 = 2;
	private static final float PLAYER_SPEED = 3.0f;

	private float gameDuration;

	private Level mLevel;

	private PlayerInput mPlayerInput;

	private boolean finished;
	private int endStatus;
	private static final int NOT_ENDED = 0;
	private static final int WIN = 1;
	private static final int LOST_KILLED = 2;
	private static final int LOST_TIMEOVER = 3;

	private boolean isSingleplayer;

	private List<Character> mPlayers;
	private List<Droid> mDroids;
	private List<Bomb> mBombs;

	private Resource mResources;

	public Game(Resource resources, Level level, boolean isSingleplayer) {
		super();
		this.finished = false;
		this.endStatus = NOT_ENDED;

		this.isSingleplayer = isSingleplayer;

		this.mLevel = level;
		this.gameDuration = level.getGameDuration() * 1000; //to ms
		this.mBombs = new ArrayList<Bomb>();

		mResources = resources;
		if (this.isSingleplayer) {
			this.mPlayerInput = new LocalPlayerInput(this);
		} else {
			this.mPlayerInput = new RemotePlayerInput(this);
		}

		// set characters on their positions of the level
		this.setCharactersOnField();
	}

	// Get the Position for the Droids & Players from the current level(grid)
	private void setCharactersOnField() {
		// Create Lists
		this.mDroids = new ArrayList<Droid>();
		this.mPlayers = new ArrayList<Character>();

		// Creating Player and Robots on their starting position
		for (int i = 0; i < this.mLevel.getGrid().getColSize(); i++) {
			for (int j = 0; j < this.mLevel.getGrid().getRowSize(); j++) {
				switch (this.mLevel.getGrid().getGridCell(j, i)) {
				case '1':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers()) {
						mPlayers.add(new Character(
								mResources.getPlayerBitmap()[PLAYER_1], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
					}
					break;
				case '2':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers() && !this.isSingleplayer) {
						mPlayers.add(new Character(
								mResources.getPlayerBitmap()[PLAYER_2], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
					}
					break;
				case '3':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers() && !this.isSingleplayer) {
						mPlayers.add(new Character(
								mResources.getPlayerBitmap()[PLAYER_3], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
					}
					break;
				case 'R':
					this.mDroids.add(new Droid(mResources.getDroidBitmap(), j,
							i, this.mLevel.getRobotSpeed(), this, true));
					break;
				default:
					break;
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

	public void placeBomb(int id, int x, int y) {
		Bomb b = new Bomb(mResources.getBombBitmap(), getPlayerByNumber(id), x,
				y, this, mResources.getExplosionSoundComponent());
		mBombs.add(b);
	}

	/**
	 * Remove all droids and enemy players in act range and adds to player's
	 * points.
	 * 
	 * @param bomb
	 * @param actRange
	 */
	public void explosionCollision(Bomb bomb, int[] actRange) {
		Iterator<Droid> droidIt = mDroids.iterator();
		Iterator<Character> playerIt = mPlayers.iterator();
		float droidPoints = mLevel.getPointsPerRobotKilled();
		float playerPoints = mLevel.getPointsPerOpponentKilled();
		explodeDroids(bomb, actRange, droidIt, droidPoints);
		explodePlayers(bomb, actRange, playerIt, playerPoints);
	}

	private void explodePlayers(Bomb bomb, int[] actRange,
			Iterator<Character> it, float points) {
		while (it.hasNext()) {
			Character p = it.next();
			if (p.equals(bomb.getOwner())) {
				continue;
			}
			// check on collision
			boolean collidedHorizontally = ((Math.rint(bomb.getY()) == Math
					.rint(p.getY()))
					&& (actRange[Bomb.RANGE_LEFT] <= Math.rint(p.getX())) && (actRange[Bomb.RANGE_RIGHT] >= Math
					.rint(p.getX())));
			boolean collidedVertically = ((Math.rint(bomb.getX()) == Math
					.rint(p.getX()))
					&& (actRange[Bomb.RANGE_UP] <= Math.rint(p.getY())) && (actRange[Bomb.RANGE_DOWN] >= Math
					.rint(p.getY())));

			if (collidedHorizontally || collidedVertically) {
				p.kill();
				Character owner = bomb.getOwner();
				if (mPlayers.contains(owner)) {
					owner.setPoints(owner.getPoints() + points);
				}
			}
		}
	}

	private void explodeDroids(Bomb bomb, int[] actRange, Iterator<Droid> it,
			float points) {
		while (it.hasNext()) {
			Droid d = it.next();
			// check on collision
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
				Character owner = bomb.getOwner();
				if (mPlayers.contains(owner)) {
					owner.setPoints(owner.getPoints() + points);
				}
			}
		}
	}

	public void update(long timePassed) {
		gameDuration -= timePassed;
		for (Character p : mPlayers) {
			if (p.isAlive()) {
				p.update(timePassed);
			}
		}
		List<Bomb> bombsToRemove = new ArrayList<Bomb>();
		for (Bomb b : mBombs) {
			float t = b.update(timePassed);
			if (t < -b.getExplosionDuration()) {
				bombsToRemove.add(b);
			}
		}
		mBombs.removeAll(bombsToRemove);
		for (Droid d : mDroids) {
			d.updateDroid(timePassed);
			d.update(timePassed);
		}
		checkGameFinish();
	}

	private void checkGameFinish() {
		int playersAlive = 0;
		for (Character c : mPlayers) {
			if (c.isAlive()) {
				playersAlive++;
			}
		}
		if (gameDuration <= 0 || playersAlive < 1
				|| (playersAlive == 1 && this.mDroids.size() == 0)) {
			this.finished = true;
			int playerId = mPlayerInput.getPlayerId();

			if (this.mDroids.size() == 0
					&& this.mPlayers.get(playerId).isAlive()) {
				this.endStatus = WIN;
			} else if (!this.mPlayers.get(playerId).isAlive()) {
				this.endStatus = LOST_KILLED;
			} else if (gameDuration <= 0) {
				this.endStatus = LOST_TIMEOVER;
			}
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
					canvas.drawBitmap(mResources.getWallBitMap(), rowNum
							* mResources.getWallBitMap().getWidth(), collNum
							* mResources.getWallBitMap().getHeight(), null);
					break;
				}
				case LevelGrid.OBSTACLE: {
					canvas.drawBitmap(mResources.getObstacleBitmap(), rowNum
							* mResources.getObstacleBitmap().getWidth(),
							collNum
									* mResources.getObstacleBitmap()
											.getHeight(), null);
					break;
				}
				}
			}
		}

		for (Character c : mPlayers) {
			if (c.isAlive()) {
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

	public int getLeftOpponents() {
		int counter = 0;
		for (int i = 0; i < this.mPlayers.size(); i++) {
			if (this.mPlayers.get(i).isAlive()) {
				counter++;
			}
		}
		return (counter - 1 + this.mDroids.size());
	}

	/**
	 * GETTERS & SETTERS
	 */
	public float getTimeLeft() {
		return gameDuration;
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

	public List<Droid> getDroids() {
		return mDroids;
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

	public PlayerInput getPlayerInput() {
		return mPlayerInput;
	}
}
