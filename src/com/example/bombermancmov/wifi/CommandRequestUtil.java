package com.example.bombermancmov.wifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.bombermancmov.model.Droid;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.wifi.commands.Command;
import com.example.bombermancmov.wifi.commands.DroidMovementCommand;

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
		CommandRequest droidMovCmdReq = extractDroidMovement(commandRequests, game);
		commandRequests.add(droidMovCmdReq);
		return commandRequests;
	}

	private static CommandRequest extractDroidMovement(
			List<CommandRequest> commandRequests, Game game) {
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
