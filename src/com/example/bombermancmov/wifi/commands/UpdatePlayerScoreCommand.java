package com.example.bombermancmov.wifi.commands;

import java.util.List;

import com.example.bombermancmov.model.Game;

public class UpdatePlayerScoreCommand extends Command {

	public static final String CODE = "us";
	private static final int ARG_POINTS = 0;
	private Game mGame;

	public UpdatePlayerScoreCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}

	/**
	 * This method should receive as unique argument, number of
	 * points the player earned
	 * 
	 */
	
	@Override
	public void execute(List<String> args) {
		int points = Integer.parseInt(args.get(ARG_POINTS));
		int mID = mGame.getPlayerInput().getPlayerId();
		float actualScore = mGame.getPlayerByNumber(mID).getPoints();
		float newScore = actualScore + (points * 1.0f);
		mGame.getPlayerByNumber(mID).setPoints(newScore);
	}
}
