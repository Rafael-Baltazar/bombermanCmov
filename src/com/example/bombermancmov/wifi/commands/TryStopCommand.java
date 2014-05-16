package com.example.bombermancmov.wifi.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

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
		Log.d("STOP", "Stopping player");
		int playerId;
		for(String s : args){
			playerId = Integer.parseInt(s);
			mGame.getPlayerByNumber(playerId).stop();
			mGame.addStopedPlayer(playerId);
		}
	}
	
	public static CommandRequest extractCommandRequest(Game game) {
		List<String> args = new ArrayList<String>();
		for(Integer i : game.getStopedPlayers()){
			Log.d("STOP", "Sending stopping player - " + i);
			args.add(Integer.toString(i));
		}
		CommandRequest cmdRequest = new CommandRequest(TryStopCommand.CODE,
				args);
		return cmdRequest;
	}

}
