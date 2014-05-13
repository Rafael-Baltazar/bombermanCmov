package com.example.bombermancmov;

public class LocalPlayerInput extends PlayerInput {
	private int playerId = 0; // single-player
	private MainGamePanel mGamePanel;

	/**
	 * @param mGamePanel
	 */
	public LocalPlayerInput(MainGamePanel mGamePanel) {
		super();
		this.mGamePanel = mGamePanel;
	}

	@Override
	public void tryMoveUp() {
		mGamePanel.tryUp(playerId);
	}

	@Override
	public void tryMoveDown() {
		mGamePanel.tryDown(playerId);
	}

	@Override
	public void tryMoveLeft() {
		mGamePanel.tryLeft(playerId);
	}

	@Override
	public void tryMoveRight() {
		mGamePanel.tryRight(playerId);
	}

	@Override
	public void tryStop() {
		mGamePanel.tryStop(playerId);
	}

	@Override
	public void placeBomb() {
		mGamePanel.placeBomb(playerId);
	}

}
