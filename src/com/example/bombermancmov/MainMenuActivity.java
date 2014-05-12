package com.example.bombermancmov;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.example.bombermancmov.wifi.WifiMessage;
import com.example.bombermancmov.wifi.WifiMessageParser;
import com.example.bombermancmov.wifi.WifiService;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainMenuActivity extends Activity {
	private static final String TAG = MainMenuActivity.class.getSimpleName();

	// WifiP2p
	private static final int REMOTE_PORT = 10001;
	private WifiService mWifi = new WifiService(this);

	// Layout
	private EditText ePlayerName;

	// Activity life-cycle
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		// WifiP2p
		SimWifiP2pSocketManager.Init(getApplicationContext());

		// Layout
		ePlayerName = (EditText) findViewById(R.id.ePlayerName);
	}

	@Override
	protected void onResume() {
		mWifi.bindService();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mWifi.stopService();
		super.onPause();
	}

	public void newSingleGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, SingleGameActivity.class);
		intent.putExtra("PlayerName", name);
		startActivity(intent);
	}
	
	public void newMultiPlayerGame(View v) {
		String name = ePlayerName.getText().toString();
		new WriteToTask().execute(new String[] { "192.168.0.2", name });
//		Intent intent = new Intent(this, LobbyActivity.class);
//		startActivity(intent);
	}

	public void newServer(View v) {
		Intent intent = new Intent(this, ServerActivity.class);
		startActivity(intent);
	}

	public void exit(View v) {
		finish();
	}

	/**
	 * Send a message to a virtual address. Use: (new
	 * WriteToTask).execute(virtualHost,message).
	 */
	public class WriteToTask extends AsyncTask<String, Void, String> {
		String message;
		SimWifiP2pSocket clientSocket;

		@Override
		protected String doInBackground(String... params) {
			String serverHost = params[0];
			message = params[1];
			try {
				clientSocket = new SimWifiP2pSocket(serverHost, REMOTE_PORT);
			} catch (UnknownHostException e) {
				return "Unknown Host:" + e.getMessage();
			} catch (IOException e) {
				return "IO error:" + e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Log.e(TAG, result);
			} else {
				List<String> args = new ArrayList<String>();
				String message = ePlayerName.getText().toString();
				args.add(message);
				WifiMessage wMsg = new WifiMessage("rcv", args);
				List<WifiMessage> wMsgs = new ArrayList<WifiMessage>();
				wMsgs.add(wMsg);
				
				String msg = WifiMessageParser.codeWifiMessages(wMsgs);
				Log.d(TAG, msg);
				try {
					ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
					os.flush();
					os.writeObject(msg);
					Log.d(TAG, "Sent message: " + message);
					ePlayerName.setText("");
				} catch (IOException e) {
					Log.e(TAG, "IO error: " + e.getMessage());
					ePlayerName.setText("");
				}
			}
		}
	}

}
