package com.example.bombermancmov.wifi.commands;

import java.util.List;

import com.example.bombermancmov.model.Game;

public class TryStopCommand extends Command {

	public static final String CODE = "ts";
	private static final int ARG_PLAYER_ID = 0;
	private Game mGame;

	/**
	 * @param mGame
	 */
	public TryStopCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}

	@Override
	public void execute(List<String> args) {
		int playerId = Integer.parseInt(args.get(ARG_PLAYER_ID));
		mGame.getPlayerByNumber(playerId).stop();
	}

}
