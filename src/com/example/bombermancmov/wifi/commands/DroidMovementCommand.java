package com.example.bombermancmov.wifi.commands;

import java.util.ArrayList;
import java.util.List;

import com.example.bombermancmov.model.Droid;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.CommandRequest;

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
		List<Droid> toRemove = new ArrayList<Droid>();
		int argIndex = 0;
		for(Droid d : droids) {
			if(argIndex < args.size()){
				float x = Float.parseFloat(args.get(argIndex++));
				float y = Float.parseFloat(args.get(argIndex++));
				d.setX(x);
				d.setY(y);
			}else {
				toRemove.add(d);
			}
		}
		for(Droid d : toRemove){
			droids.remove(d);
		}
	}
	
	public static CommandRequest extractCommandRequest(Game game) {
		List<String> args = new ArrayList<String>();
		List<Droid> droids = game.getDroids();
		for (Droid d : droids) {
			float x = d.getX();
			float y = d.getY();
			args.add(String.valueOf(x));
			args.add(String.valueOf(y));
		}
		CommandRequest cmdRequest = new CommandRequest(
				DroidMovementCommand.CODE, args);
		return cmdRequest;
	}

}
