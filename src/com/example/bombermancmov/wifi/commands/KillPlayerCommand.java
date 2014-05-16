package com.example.bombermancmov.wifi.commands;

import java.util.List;

import com.example.bombermancmov.model.Game;

public class KillPlayerCommand extends Command {

	public static final String CODE = "kp";
	private static final int ARG_PLAYER_ID = 0;
	private Game mGame;

	public KillPlayerCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}
	
	/**
	 * This method should receive a unique argument, the ID of 
	 * the player that died
	 * 
	 */
	@Override
	public void execute(List<String> args) {
		int targID = Integer.parseInt(args.get(ARG_PLAYER_ID));
		mGame.getPlayerByNumber(targID).setAlive(false);
	}
}
