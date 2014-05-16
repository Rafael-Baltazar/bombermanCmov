package com.example.bombermancmov.wifi.commands;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.bombermancmov.model.Bomb;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

public class UpdateTimeCommand extends Command {

	public static final String CODE = "ut";
	private static final int ARG_TIME_VALUE = 0;
	private Game mGame;

	public UpdateTimeCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}

	/**
	 * This method should receive as unique argument, the time left
	 * until the end of the game.
	 * 
	 */
	@Override
	public void execute(List<String> args) {
		float raw = Float.parseFloat(args.get(ARG_TIME_VALUE));
		int timeLeft = (int) Math.rint(raw);
		float timePass = mGame.getTimeLeft() - raw;
		mGame.setTimeLeft(timeLeft);
		Log.d("TIMEPASS", Float.toString(timePass));
		mGame.updateRoundPeer((long)Math.rint(timePass));
	}
	
	public static CommandRequest extractCommandRequest(Game game) {
		List<String> args = new ArrayList<String>();
		args.add(ARG_TIME_VALUE, String.valueOf(game.getTimeLeft()));

		CommandRequest cmdRequest = new CommandRequest(
				UpdateTimeCommand.CODE, args);
		return cmdRequest;
	}
}
