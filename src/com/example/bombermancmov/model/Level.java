package com.example.bombermancmov.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Level {
	private String levelName;
	private long gameDuration;

	private float explosionDuration;
	private float explosionRange;
	private int explosionTimeout;
	private float robotSpeed;
	private float pointsPerRobotKilled;
	private float pointsPerOpponentKilled;
	private float gameDuration;

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
	
	private class LevelSpecification{
		private Map<String, Method> methodList = new HashMap<String, Method>();
		
		public void initMethods(Level l)
		{
			methodList.put("LN", new SetLevelName(l));
			methodList.put("GD", new SetGameDuration(l));
			
		}
		
	}
	private abstract class Method{
		
		private Level l;
		public abstract void execute(String arg);
		public Level getL() {
			return l;
		}
		public void setL(Level l) {
			this.l = l;
		}
		
		
	}
	private class SetLevelName extends Method{

		public SetLevelName(Level l){
			setL(l);
		}
		
		@Override
		public void execute(String arg) {
			getL().levelName = arg;
		}
	}
	private class SetGameDuration extends Method{

		public SetGameDuration(Level l){
			setL(l);
		}
		
		@Override
		public void execute(String arg) {
			getL().gameDuration = Float.parseFloat(arg);
		}
	}
	

	private void readLevelFromFile(Resource resource, int level){
		int line_n;
		String line;
		String []parts;
		BufferedReader bis = new BufferedReader(new InputStreamReader(resource.getLevelResource(level)));
		
			
		
		
		resource.closeLevelResource(level);
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
