package com.example.bombermancmov.wifi.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

public class TryMoveCommand extends Command {

	public static final String CODE = "tm";
	private static final int ARG_PLAYER_ID = 0;
	private static final int ARG_PLAYER_DIR = 1;
	private Game mGame;
	
	/**
	 * @param mGame
	 */
	public TryMoveCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}

	@Override
	public void execute(List<String> args) {
		int agrIndex = 0;
		int playerId, dir;
		Character player;
		while(agrIndex < args.size()){
			
			playerId = Integer.parseInt(args.get(agrIndex++));
			dir = Integer.parseInt(args.get(agrIndex++));
			player = mGame.getPlayerByNumber(playerId);
			mGame.addPlayerMovement(playerId, dir);
			Log.d("PLAYERMOV", "Player " + playerId + "moved " + dir);
			switch(dir) {
			case Character.BACK:
				player.tryMoveUp();
				break;
			case Character.FRONT:
				player.tryMoveDown();
				break;
			case Character.LEFT:
				player.tryMoveLeft();
				break;
			case Character.RIGHT:
				player.tryMoveRight();
				break;
			}
		}
	}
	
	public static CommandRequest extractCommandRequest(Game game) {
		List<String> args = new ArrayList<String>();
		Map<Integer, Integer> moviments = game.getPlayerMovments();;
		for(Integer i : moviments.keySet()){
			Log.d("PLAYERMOV", "Sending Player " + i + "moved " + moviments.get(i));
			args.add(Integer.toString(i));
			args.add(Integer.toString(moviments.get(i)));
		}
		CommandRequest cmdRequest = new CommandRequest(TryMoveCommand.CODE,
				args);
		return cmdRequest;
	}
	

}
