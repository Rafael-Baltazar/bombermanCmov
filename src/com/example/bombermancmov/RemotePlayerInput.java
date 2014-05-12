package com.example.bombermancmov;

import java.util.ArrayList;
import java.util.List;
import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.WifiMessage;
import com.example.bombermancmov.wifi.commands.MovePlayerCommand;

public class RemotePlayerInput extends PlayerInput {
	private int playerId;
	private Game mGame;
	private WifiMessage bombPlacement;
	private WifiMessage movement;

	/**
	 * @param mGame
	 */
	public RemotePlayerInput(Game mGame) {
		super();
		this.mGame = mGame;
	}

	@Override
	public void tryMoveUp() {
		List<String> args = new ArrayList<String>();
		float x = mGame.getPlayerByNumber(playerId).getX();
		float y = mGame.getPlayerByNumber(playerId).getY();
		args.add(String.valueOf(playerId));
		args.add(String.valueOf(x));
		args.add(String.valueOf(y));
		args.add(String.valueOf(Character.BACK));
		if (movement == null) {
			movement = new WifiMessage(MovePlayerCommand.CODE, args);
		} else {
			movement.setArgs(args);
		}
	}

	@Override
	public void tryMoveDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tryMoveLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tryMoveRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tryStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void placeBomb() {
		// TODO Auto-generated method stub

	}

	public WifiMessage getBombPlacement() {
		return bombPlacement;
	}

	public WifiMessage getMovement() {
		return movement;
	}

}
