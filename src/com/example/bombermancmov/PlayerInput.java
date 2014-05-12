package com.example.bombermancmov;

public abstract class PlayerInput {
	public abstract void tryMoveUp();

	public abstract void tryMoveDown();

	public abstract void tryMoveLeft();

	public abstract void tryMoveRight();
	
	public abstract void tryStop();

	public abstract void placeBomb();
}
