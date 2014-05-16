package com.example.bombermancmov.wifi.commands;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.bombermancmov.model.Bomb;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

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
		Log.d("R", "Exec " + args.size());
		for (String s : args) {
			int playerId = Integer.parseInt(s);
			int bombX = (int) Math.rint(mGame.getPlayerByNumber(playerId)
					.getX());
			int bombY = (int) Math.rint(mGame.getPlayerByNumber(playerId)
					.getY());
			mGame.placeBomb(playerId, bombX, bombY);
			Log.d("R", "Placed bomb");
		}
	}

	public static CommandRequest extractCommandRequest(Game game) {
		List<String> args = new ArrayList<String>();
		for (Bomb b : game.getNewBombs()) {
			Log.d("R", "Sending bomb " + b.getOwnerId());
			args.add(String.valueOf(b.getOwnerId()));
		}

		CommandRequest cmdRequest = new CommandRequest(PlaceBombCommand.CODE,
				args);
		return cmdRequest;
	}

}
