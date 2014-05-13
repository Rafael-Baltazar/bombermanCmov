package com.example.bombermancmov.wifi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

import com.example.bombermancmov.wifi.commands.Command;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;
import android.os.AsyncTask;
import android.util.Log;

public class RegisterServer extends AsyncTask<Void, List<CommandRequest>, Void> {
	private static final String TAG = RegisterServer.class.getSimpleName();

	private SimWifiP2pSocketServer mSrv;
	private Map<String, Command> mCmds;

	public RegisterServer(SimWifiP2pSocketServer server,
			Map<String, Command> serverCommands) {
		mSrv = server;
		mCmds = serverCommands;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// Accept. Read command. Execute.
		while (!Thread.currentThread().isInterrupted()) {
			SimWifiP2pSocket client = null;
			try {
				// Accept.
				client = mSrv.accept();
			} catch (IOException e) {
				Log.e(TAG, "Server Accept " + e.getMessage());
				return null;
			}
			try {
				// Read command.
				ObjectInputStream is = new ObjectInputStream(
						client.getInputStream());
				String message = (String) is.readObject();
				is.close();
				client.close();
				// Execute.
				Log.d(TAG, message);
				List<CommandRequest> wifiCmds = CommandRequestParser
						.translateCommandRequestString(message);
				publishProgress(wifiCmds);
			} catch (IOException e) {
				Log.e(TAG, "Commands read " + e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(List<CommandRequest>... values) {
		List<CommandRequest> wifiCmds = values[0];
		for (CommandRequest m : wifiCmds) {
			mCmds.get(m.getCommand()).execute(m.getArgs());
		}
		super.onProgressUpdate(values);
	}

}
