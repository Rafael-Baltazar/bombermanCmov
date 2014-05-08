package com.example.bombermancmov.model;

import java.util.List;

/**
 * Used to change the direction of the droids movement.
 */
public class DroidAI {

	/** Change the direction of the droids every TICK milliseconds. */
	private static long TIME_TO_MOVE = 1500;
	private long timeToProcess = 0;
	private List<Droid> droids;

	/**
	 * @param droids
	 */
	public DroidAI(List<Droid> droids) {
		super();
		this.droids = droids;
	}

	public void update(long timePassed) {
		timeToProcess += timePassed;
		if (timeToProcess > TIME_TO_MOVE) {
			for (Droid d : droids) {
				d.tryMoveRandomly();
			}
			timeToProcess = 0;
		}
	}
}
