package com.example.bombermancmov;

import java.util.ArrayList;
import java.util.List;
import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;
import com.example.bombermancmov.wifi.commands.PlaceBombCommand;
import com.example.bombermancmov.wifi.commands.TryMoveCommand;
import com.example.bombermancmov.wifi.commands.TryStopCommand;

public class RemotePlayerInput extends PlayerInput {
	private int mPlayerId;
	private Game mGame;
	private CommandRequest bombPlacement = null;
	private CommandRequest movement = null;

	/**
	 * @param mGame
	 * @param playerId
	 */
	public RemotePlayerInput(Game game, int playerId) {
		super();
		mGame = game;
		mPlayerId = playerId;
	}

	@Override
	public void tryMoveUp() {
		tryMove(Character.BACK);
	}

	@Override
	public void tryMoveDown() {
		tryMove(Character.FRONT);
	}

	@Override
	public void tryMoveLeft() {
		tryMove(Character.LEFT);
	}

	@Override
	public void tryMoveRight() {
		tryMove(Character.RIGHT);
	}
	
	private void tryMove(int direction) {
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(mPlayerId));
		args.add(String.valueOf(direction));
		if (movement == null) {
			movement = new CommandRequest(TryMoveCommand.CODE, args);
		} else {
			movement.setArgs(args);
		}
	}
	
	@Override
	public void tryStop() {
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(mPlayerId));
		movement = new CommandRequest(TryStopCommand.CODE, args);
	}

	@Override
	public void placeBomb() {
		List<String> args = new ArrayList<String>();
		Character player = mGame.getPlayerByNumber(mPlayerId);
		int x = (int) Math.rint(player.getX());
		int y = (int) Math.rint(player.getY());
		args.add(String.valueOf(mPlayerId));
		args.add(String.valueOf(x));
		args.add(String.valueOf(y));
		bombPlacement = new CommandRequest(PlaceBombCommand.CODE, args);
	}

	public CommandRequest getBombPlacement() {
		return bombPlacement;
	}

	public CommandRequest getMovement() {
		return movement;
	}

}
