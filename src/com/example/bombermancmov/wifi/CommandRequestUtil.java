package com.example.bombermancmov.wifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.commands.Command;
import com.example.bombermancmov.wifi.commands.DroidMovementCommand;
import com.example.bombermancmov.wifi.commands.KillPlayerCommand;
import com.example.bombermancmov.wifi.commands.PlaceBombCommand;
import com.example.bombermancmov.wifi.commands.TryMoveCommand;
import com.example.bombermancmov.wifi.commands.TryStopCommand;
import com.example.bombermancmov.wifi.commands.UpdateTimeCommand;

/**
 * Utility methods to translate command requests from a string, code command
 * requests into a string and execute command requests given a command list.
 */
public class CommandRequestUtil {

	private static final String cmdDelim = "@";
	private static final String argDelim = ":";

	/**
	 * @param commands
	 * @return the list of command requests
	 */
	public static List<CommandRequest> translateCommandRequestString(
			String commands) {
		List<CommandRequest> result = new ArrayList<CommandRequest>();
		if (commands.equals("")) {
			return result;
		}
		String[] splitMsgs = commands.split(cmdDelim);

		for (String s : splitMsgs) {
			String cmd;
			List<String> args = new ArrayList<String>();

			String[] msg = s.split(argDelim);
			if (msg.length > 0) {
				cmd = msg[0];
				for (int i = 1; i < msg.length; i++) {
					args.add(msg[i]);
				}
				result.add(new CommandRequest(cmd, args));
			}
		}
		return result;
	}

	/**
	 * @param msgs
	 * @return string to send through wifiP2p
	 */
	public static String codeCommandRequests(List<CommandRequest> msgs) {
		StringBuilder result = new StringBuilder();

		String delCmd = "";
		for (CommandRequest wM : msgs) {
			String cmd = wM.getCommand();
			List<String> args = wM.getArgs();

			StringBuilder wifiMessage = new StringBuilder(cmd);

			for (String arg : args) {
				wifiMessage.append(argDelim).append(arg);
			}

			result.append(delCmd).append(wifiMessage.toString());
			delCmd = cmdDelim;
		}
		return result.toString();
	}

	/**
	 * Executes the given command requests given a command map.
	 * 
	 * @param cmdRequests
	 * @param commandMap
	 */
	public static void executeCommandRequests(List<CommandRequest> cmdRequests,
			Map<String, Command> commandMap) {
		for (CommandRequest c : cmdRequests) {
			Command cmd = commandMap.get(c.getCommand());
			cmd.execute(c.getArgs());
		}
	}

	/**
	 * Analyzes the game object in order to create game updates to be sent by
	 * the master to its peers.
	 * 
	 * @param game
	 * @return the list of game updates in form of command requests
	 */
	public static List<CommandRequest> extractCommandRequests(Game game) {
		List<CommandRequest> commandRequests = new ArrayList<CommandRequest>();
		
		CommandRequest droidMovCmdReq = DroidMovementCommand
				.extractCommandRequest(game);
		commandRequests.add(droidMovCmdReq);
		
		CommandRequest updateTimeCmdReq = UpdateTimeCommand
				.extractCommandRequest(game);
		commandRequests.add(updateTimeCmdReq);
		
		if(game.hasNewBombs()){
			CommandRequest placeBombCmdReq = PlaceBombCommand
					.extractCommandRequest(game);
			commandRequests.add(placeBombCmdReq);
		}
		if(game.playerMoved()){
			CommandRequest tryMoveCmdReq = TryMoveCommand
					.extractCommandRequest(game);
			commandRequests.add(tryMoveCmdReq);
		}
		if(game.playerStoped()){
			CommandRequest trStopCmdReq = TryStopCommand
					.extractCommandRequest(game);
			commandRequests.add(trStopCmdReq);
			
		}
		/*
		if(game.hasDeadPlayers()){
			CommandRequest killPlayerCmdReq = KillPlayerCommand
					.extractCommandRequest(game);
			commandRequests.add(killPlayerCmdReq);
		}*/
		
		
		return commandRequests;
	}

}
