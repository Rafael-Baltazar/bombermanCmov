package com.example.bombermancmov.wifi.commands;

import java.util.List;

import com.example.bombermancmov.model.Droid;
import com.example.bombermancmov.model.Game;

public class DroidMovementCommand extends Command {

	public static final String CODE = "dm";
	private Game mGame;
	
	/**
	 * @param mGame
	 */
	public DroidMovementCommand(Game mGame) {
		super();
		this.mGame = mGame;
	}

	@Override
	public void execute(List<String> args) {
		List<Droid> droids = mGame.getDroids();
		int argIndex = 0;
		for(Droid d : droids) {
			float x = Float.parseFloat(args.get(argIndex++));
			float y = Float.parseFloat(args.get(argIndex++));
			d.setX(x);
			d.setY(y);
		}
	}

}
