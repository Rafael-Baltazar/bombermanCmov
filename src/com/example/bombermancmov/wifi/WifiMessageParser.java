package com.example.bombermancmov.wifi;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Use json.
 */
public class WifiMessageParser {
	private static final String TAG = WifiMessageParser.class.getSimpleName();

	private static final String cmdDelim = "@";
	private static final String argDelim = ":";

	/**
	 * @param commands
	 * @return the list of wifiMessages
	 */
	public static List<WifiMessage> decodeWifiMessages(String commands) {
		List<WifiMessage> result = new ArrayList<WifiMessage>();
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
				result.add(new WifiMessage(cmd, args));
			}
		}
		return result;
	}

	/**
	 * @param msgs
	 * @return string to send through wifiP2p
	 */
	public static String codeWifiMessages(List<WifiMessage> msgs) {
		StringBuilder result = new StringBuilder();

		String delCmd = "";
		for (WifiMessage wM : msgs) {
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

}
