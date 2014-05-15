package com.example.bombermancmov.wifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.bombermancmov.wifi.commands.Command;

/**
 * Util methods to translate command requests from a string, code command
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

	public static void executeCommandRequests(List<CommandRequest> cmdRequests,
			Map<String, Command> commands) {
		for (CommandRequest c : cmdRequests) {
			Command cmd = commands.get(c.getCommand());
			cmd.execute(c.getArgs());
		}
	}
}
