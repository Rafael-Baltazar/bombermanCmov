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
	private static final int UNINITIALIZED_ID = -1;
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

	/**
	 * @param mGame
	 */
	public RemotePlayerInput(Game mGame) {
		this(mGame, UNINITIALIZED_ID);
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
		if (mPlayerId == UNINITIALIZED_ID) {
			return;
		}
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(mPlayerId));
		args.add(String.valueOf(direction));
		movement = new CommandRequest(TryMoveCommand.CODE, args);
	}

	@Override
	public void tryStop() {
		if (mPlayerId == UNINITIALIZED_ID) {
			return;
		}
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(mPlayerId));
		movement = new CommandRequest(TryStopCommand.CODE, args);
	}

	@Override
	public void placeBomb() {
		if (mPlayerId == UNINITIALIZED_ID) {
			return;
		}
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

	@Override
	public int getPlayerId() {
		return mPlayerId;
	}

	public void setGame(Game mGame) {
		this.mGame = mGame;
	}

	@Override
	public void setPlayerId(int id) {
		mPlayerId = id;
	}

	@Override
	public List<CommandRequest> consumeCommandRequests() {
		List<CommandRequest> commandRequests = new ArrayList<CommandRequest>();
		if (movement != null) {
			commandRequests.add(movement);
			movement = null;
		}
		if (bombPlacement != null) {
			commandRequests.add(bombPlacement);
			bombPlacement = null;
		}
		return commandRequests;
	}

}
