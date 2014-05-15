package com.example.bombermancmov.wifi.commands;

import java.util.List;

import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;

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
		int playerId = Integer.parseInt(args.get(ARG_PLAYER_ID));
		int dir = Integer.parseInt(args.get(ARG_PLAYER_DIR));
		Character player = mGame.getPlayerByNumber(playerId);
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
