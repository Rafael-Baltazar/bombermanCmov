package com.example.bombermancmov;

import java.util.List;

import com.example.bombermancmov.wifi.CommandRequest;

public abstract class PlayerInput {
	public abstract void tryMoveUp();

	public abstract void tryMoveDown();

	public abstract void tryMoveLeft();

	public abstract void tryMoveRight();

	public abstract void tryStop();

	public abstract void placeBomb();

	public abstract int getPlayerId();

	public abstract void setPlayerId(int id);
	
	public abstract List<CommandRequest> consumeCommandRequests(); 
}
