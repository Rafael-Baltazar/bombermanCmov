package com.example.bombermancmov;

import java.util.ArrayList;
import java.util.List;

import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

public class LocalPlayerInput extends PlayerInput {
	private int mPlayerId = 0; // single-player
	private Game mGame;

	/**
	 * @param game
	 */
	public LocalPlayerInput(Game game) {
		super();
		this.mGame = game;
	}

	@Override
	public void tryMoveUp() {
		mGame.getPlayerByNumber(mPlayerId).tryMoveUp();
	}

	@Override
	public void tryMoveDown() {
		mGame.getPlayerByNumber(mPlayerId).tryMoveDown();
	}

	@Override
	public void tryMoveLeft() {
		mGame.getPlayerByNumber(mPlayerId).tryMoveLeft();
	}

	@Override
	public void tryMoveRight() {
		mGame.getPlayerByNumber(mPlayerId).tryMoveRight();
	}

	@Override
	public void tryStop() {
		mGame.getPlayerByNumber(mPlayerId).stop();
	}

	@Override
	public void placeBomb() {
		Character player = mGame.getPlayerByNumber(mPlayerId);
		int x = (int) Math.rint(player.getX());
		int y = (int) Math.rint(player.getY());
		mGame.placeBomb(mPlayerId, x, y);
	}

	@Override
	public int getPlayerId() {
		return mPlayerId;
	}

	@Override
	public void setPlayerId(int id) {
		mPlayerId = id;
	}

	@Override
	public List<CommandRequest> consumeCommandRequests() {
		return new ArrayList<CommandRequest>();
	}

}
