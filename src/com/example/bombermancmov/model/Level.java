package com.example.bombermancmov.model;

public class Level {
	private String levelName;

	private float explosionDuration;
	private float explosionRange;
	private int explosionTimeout;
	private float robotSpeed;
	private float pointsPerRobotKilled;
	private float pointsPerOpponentKilled;
	private long gameDuration;

	private int maxNumberPlayers;
	
	private LevelGrid grid;
	
	public Level() {
		super();
	}
	
	public Level(Resource res, int level_number){
		
	}

	public Level(String levelName, float explosionDuration, float explosionRange, float robotSpeed,
			float pointsPerRobotKilled, float pointsPerOpponentKilled,
			int maxNumberPlayers, int explosionTimeout, LevelGrid grid) {
		super();
		this.levelName = levelName;
		this.explosionDuration = explosionDuration;
		this.explosionRange = explosionRange;
		this.robotSpeed = robotSpeed;
		this.pointsPerRobotKilled = pointsPerRobotKilled;
		this.pointsPerOpponentKilled = pointsPerOpponentKilled;
		this.maxNumberPlayers = maxNumberPlayers;
		this.explosionTimeout = explosionTimeout;
		this.grid = grid;
	}
	
	/**
	 * GETTER &
	 * SETTERs
	 */
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
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

	public int getMaxNumberPlayers() {
		return maxNumberPlayers;
	}

	public void setMaxNumberPlayers(int maxNumberPlayers) {
		this.maxNumberPlayers = maxNumberPlayers;
	}

	public int getExplosionTimeout() {
		return explosionTimeout;
	}

	public void setExplosionTimeout(int explosionTimeout) {
		this.explosionTimeout = explosionTimeout;
	}

	public LevelGrid getGrid() {
		return grid;
	}

	public void setGrid(LevelGrid grid) {
		this.grid = grid;
	}
	
	public long getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(long gameDuration) {
		this.gameDuration = gameDuration;
	}
}
