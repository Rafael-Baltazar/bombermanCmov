package com.example.bombermancmov.wifi.commands;

import java.util.List;

import com.example.bombermancmov.model.Game;

public class PlaceBombCommand extends Command {

	public static final String CODE = "pb";
	private static final int ARG_PLAYER_ID = 0;
	private static final int ARG_PLAYER_X = 1;
	private static final int ARG_PLAYER_Y = 2;
	private Game mGame;
	
	
	
	public PlaceBombCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}



	@Override
	public void execute(List<String> args) {
		int playerId = Integer.parseInt(args.get(ARG_PLAYER_ID));
		
		int bombX = (int) Math.rint(mGame.getPlayerByNumber(playerId).getX());
		int bombY = (int) Math.rint(mGame.getPlayerByNumber(playerId).getY());
		
		mGame.placeBomb(playerId, bombX, bombY);
	}

}
