package com.example.bombermancmov.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	public static final int PLAYER_4 = 3;
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

	private Map<Integer, Character> mPlayers;
	private List<Droid> mDroids;
	private List<Bomb> mBombs;
	
	private List<Bomb> newPlacedBombs;
	private List<Bomb> explodedBombs;
	
	private Map<Integer, Integer> walkMovements;
	private List<Integer> stopMovements;
	private boolean hasDeadPlayers;
	

	private Resource mResources;

	public Game(Resource resources, Level level, boolean isSingleplayer) {
		super();
		this.finished = false;
		this.endStatus = NOT_ENDED;

		this.isSingleplayer = isSingleplayer;

		this.mLevel = level;
		this.gameDuration = level.getGameDuration() * 1000; //to ms
		this.mBombs = new ArrayList<Bomb>();
		this.newPlacedBombs = new ArrayList<Bomb>();
		this.explodedBombs = new ArrayList<Bomb>();
		this.walkMovements = new HashMap<Integer, Integer>();
		this.stopMovements = new ArrayList<Integer>();
		this.hasDeadPlayers = false;

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
		this.mPlayers = new HashMap<Integer, Character>();

		// Creating Player and Robots on their starting position
		for (int i = 0; i < this.mLevel.getGrid().getColSize(); i++) {
			for (int j = 0; j < this.mLevel.getGrid().getRowSize(); j++) {
				switch (this.mLevel.getGrid().getGridCell(j, i)) {
				case '1':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers()) {
						mPlayers.put(0, new Character(
								mResources.getPlayerBitmap()[PLAYER_1], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
					}
					break;
				case '2':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers() && !this.isSingleplayer) {
						mPlayers.put(1, new Character(
								mResources.getPlayerBitmap()[PLAYER_2], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
					}
					break;
				case '3':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers() && !this.isSingleplayer) {
						mPlayers.put(2, new Character(
								mResources.getPlayerBitmap()[PLAYER_3], j, i,
								PLAYER_SPEED, getLevel().getGrid(), this, true));
					}
					break;
				case '4':
					if (this.mPlayers.size() < this.mLevel
							.getMaxNumberPlayers() && !this.isSingleplayer) {
						mPlayers.put(3, new Character(
								mResources.getPlayerBitmap()[PLAYER_4], j, i,
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

	public void setHasDeadPlayers(boolean value){
		this.hasDeadPlayers = value;
	}
	public boolean hasDeadPlayers(){
		return hasDeadPlayers;
	}
	
	
	public List<Character> getPlayersByPos(float x, float y) {
		float range = 0.01f;
		List<Character> result = new ArrayList<Character>();
		for (Character p : mPlayers.values()) {
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
		Bomb b = new Bomb(mResources.getBombBitmap(), getPlayerByNumber(id), id,x,
				y, this, mResources.getExplosionSoundComponent());
		mBombs.add(b);
		newPlacedBombs.add(b);
	}
	public boolean hasNewBombs(){
		return !newPlacedBombs.isEmpty();
	}
	
	public List<Bomb> getNewBombs(){
		List<Bomb> retList = new ArrayList<Bomb>();
		retList.addAll(newPlacedBombs);
		newPlacedBombs.clear();
		return retList;
	}
	public boolean playerMoved(){
		return !walkMovements.isEmpty();
	}
	public Map<Integer, Integer> getPlayerMovments(){
		Map<Integer, Integer> retList = new HashMap<Integer, Integer>();
		retList.putAll(walkMovements);
		walkMovements.clear();
		return retList;
	}
	public void addPlayerMovement(int player, int direction){
		walkMovements.put(player, direction);
	}
	
	public boolean playerStoped(){
		return !stopMovements.isEmpty();
	}
	
	public List<Integer> getStopedPlayers(){
		List<Integer> retList = new ArrayList<Integer>();
		retList.addAll(stopMovements);
		stopMovements.clear();
		return retList;
	}
	public void addStopedPlayer(int player){
		stopMovements.add(player);
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
		List<Character> playerList = new ArrayList<Character>();
		playerList.addAll(mPlayers.values());
		Iterator<Character> playerIt = playerList.iterator();
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
				if (mPlayers.values().contains(owner)) {
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
				if (mPlayers.values().contains(owner)) {
					owner.setPoints(owner.getPoints() + points);
				}
			}
		}
	}
	public void updateRoundPeer(long timePassed){
		for (Character p : mPlayers.values()) {
			if (p.isAlive()) {
				p.update(timePassed);
			}
		}
		List<Bomb> bombsToRemove = new ArrayList<Bomb>();
		for (Bomb b : mBombs) {
			float t = b.updateInPeed(timePassed);
			if (t < -b.getExplosionDuration()) {
				bombsToRemove.add(b);
			}
		}
		mBombs.removeAll(bombsToRemove);
	}

	public void update(long timePassed) {
		gameDuration -= timePassed;
		for (Character p : mPlayers.values()) {
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
		explodedBombs.addAll(bombsToRemove);
		mBombs.removeAll(bombsToRemove);
		for (Droid d : mDroids) {
			d.updateDroid(timePassed);
			d.update(timePassed);
		}
		checkGameFinish();
	}
	public List<Bomb> getBombs(){
		return mBombs;
	}
	public List<Bomb> getExplodedBombs(){
		List<Bomb> retList = new ArrayList<Bomb>();
		retList.addAll(explodedBombs);
		explodedBombs.clear();
		return retList;
	}
	public boolean hasExplodedBombs(){
		return !explodedBombs.isEmpty();
	}
	private void checkGameFinish() {
		//Singleplayer-Ending: You win if all droids are destroyed
		//					   You loose, if time is over
		//								  you are killed by a droid
		//Multiplayer-Endig:   You win if no opponents are left
		//					   You loose, if time is over
		//								  you are killed by anyone		
		if(this.getLeftOpponents() == 0) {
			this.finished = true;
			this.endStatus = WIN;
		} else {
			if(this.gameDuration <= 0) {
				this.finished = true;
				this.endStatus = LOST_TIMEOVER; 
			} else {
				if(this.mPlayers.size() > 1 && !this.getPlayerByNumber(this.getPlayerInput().getPlayerId()).isAlive()) {
					this.finished = false;
					this.endStatus = LOST_KILLED;
				} else {
					if(this.mPlayers.size() == 1 && !this.getPlayerByNumber(this.getPlayerInput().getPlayerId()).isAlive()) {
						this.finished = true;
						this.endStatus = LOST_KILLED;
					}
				}
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

		for (Character c : mPlayers.values()) {
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
	public void setTimeLeft(int timeLeft){
		this.gameDuration = timeLeft;
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
		return (List<Character>) mPlayers.values();
	}

	public List<Droid> getDroids() {
		return mDroids;
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
	public Map<Integer, Character> getPlayerMap(){
		return mPlayers;
	}
}
