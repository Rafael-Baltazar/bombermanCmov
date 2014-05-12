package com.example.bombermancmov;

public class LocalPlayerInput extends PlayerInput {
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
		mGamePanel.tryWalk(1);
	}

	@Override
	public void tryMoveDown() {
		mGamePanel.tryWalk(2);
	}

	@Override
	public void tryMoveLeft() {
		mGamePanel.tryWalk(0);
	}

	@Override
	public void tryMoveRight() {
		mGamePanel.tryWalk(3);
	}

	@Override
	public void tryStop() {
		mGamePanel.tryStop();
	}

	@Override
	public void placeBomb() {
		mGamePanel.doAction(4);
	}

}
