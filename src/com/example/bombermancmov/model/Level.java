package com.example.bombermancmov.model;

public class Level {
	private String levelName;
	private float gameDuration;
	private float explosionTimeout;
	private float explosionDuration;
	private float explosionRange;
	private float robotSpeed;
	private float pointsPerRobotKilled;
	private float pointsPerOpponentKilled;
	/**
	 * -	Empty
	 * W	Wall
	 * O	Obstacle
	 * R	Robot
	 * 1-3	Players
	 */
	private char[] gridLayout;
	/**
	 * @param levelName
	 * @param gameDuration
	 * @param explosionTimeout
	 * @param explosionDuration
	 * @param explosionRange
	 * @param robotSpeed
	 * @param pointsPerRobotKilled
	 * @param pointsPerOpponentKilled
	 * @param gridLayout
	 */
	public Level(String levelName, float gameDuration, float explosionTimeout,
			float explosionDuration, float explosionRange, float robotSpeed,
			float pointsPerRobotKilled, float pointsPerOpponentKilled,
			char[] gridLayout) {
		super();
		this.levelName = levelName;
		this.gameDuration = gameDuration;
		this.explosionTimeout = explosionTimeout;
		this.explosionDuration = explosionDuration;
		this.explosionRange = explosionRange;
		this.robotSpeed = robotSpeed;
		this.pointsPerRobotKilled = pointsPerRobotKilled;
		this.pointsPerOpponentKilled = pointsPerOpponentKilled;
		this.gridLayout = gridLayout;
	}
	/**
	 * To test.
	 */
	public Level() {
		super();
		this.levelName = "defaultLevelName";
		this.gameDuration = 180000; //three minutes?
		this.explosionTimeout = 1500;
		this.explosionDuration = 1000;
		this.explosionRange = 1;
		this.robotSpeed = 1; //1 cell per second
		this.pointsPerRobotKilled = 1;
		this.pointsPerOpponentKilled = 2;
		this.gridLayout = new char[] {
			'W','W','W','W','W','W','W',
			'W','-','-','-','-','-','W',
			'W','W','W','W','W','W','W'
		};
	}
	
	
}
