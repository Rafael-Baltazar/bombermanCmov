package com.example.bombermancmov.wifi.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

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
		int targID;
		int argIndex = 0;
		while(argIndex < args.size()){
			targID = Integer.parseInt(args.get(argIndex++));
			mGame.getPlayerByNumber(targID).setAlive(false);
			mGame.setHasDeadPlayers(true);
		}
	}
	
	public static CommandRequest extractCommandRequest(Game game){
		List<String> args = new ArrayList<String>();
		Map<Integer, Character> chars = game.getPlayerMap();
		for(Integer i : chars.keySet()){
			if(!chars.get(i).isAlive()){
				args.add(Integer.toString(i));
			}
		}
		CommandRequest cmdRequest = new CommandRequest(
				KillPlayerCommand.CODE, args);
		game.setHasDeadPlayers(false);
		return cmdRequest;
		
		
		
	}
}
