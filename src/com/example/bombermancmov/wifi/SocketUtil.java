package com.example.bombermancmov.wifi;

import java.io.IOException;
import java.util.Map;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;

import android.util.Log;

import com.example.bombermancmov.wifi.commands.Command;

public class SocketUtil {
	private static final String TAG = SocketUtil.class.getSimpleName();
	
	private static final int SERVER_PORT = 10001;
	
	public static void registerServer(Map<String, Command> cmds) {
		try {
			SimWifiP2pSocketServer server = new SimWifiP2pSocketServer(SERVER_PORT);
			new RegisterServer(server, cmds).execute();
		} catch (IOException e) {
			Log.e(TAG, "New Server " + e.getMessage());
		}
	}

}
