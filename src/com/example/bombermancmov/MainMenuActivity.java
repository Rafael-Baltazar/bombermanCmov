package com.example.bombermancmov;

import java.io.IOException;
import java.net.UnknownHostException;

import com.example.bombermancmov.wifi.WifiConnection;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MainMenuActivity extends Activity {
	private static final String TAG = MainMenuActivity.class.getSimpleName();

	// WifiP2p
	private static final int REMOTE_PORT = 10001;
	private WifiConnection mWifi = new WifiConnection(this);

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
		ePlayerName.setOnKeyListener(sendPlayerName);
	}

	@Override
	protected void onResume() {
		mWifi.bindWifiService();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mWifi.unbindService();
		super.onPause();
	}

	// Layout
	private OnKeyListener sendPlayerName = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				// Ignore Enter
				return true;
			}
			return false;
		}
	};

	public void newSingleGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, SingleGameActivity.class);
		intent.putExtra("PlayerName", name);
		this.startActivity(intent);
	}
	
	public void newMultiPlayerGame(View v) {
		String name = ePlayerName.getText().toString();
		new WriteToTask().execute(new String[] { "192.168.0.2", name });
	}

	public void newServer(View v) {
		Intent intent = new Intent(this, ServerActivity.class);
		this.startActivity(intent);
	}

	public void exit(View v) {
		finish();
	}

	// WifiP2p
	/**
	 * Send a message to a virtual address. Use: (new
	 * WriteToTask).execute(virtualHost,message).
	 */
	public class WriteToTask extends AsyncTask<String, Void, String> {
		String message;
		SimWifiP2pSocket clientSocket;

		@Override
		protected String doInBackground(String... params) {
			String clientHost = params[0];
			message = params[1];
			try {
				clientSocket = new SimWifiP2pSocket(clientHost, REMOTE_PORT);
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
				byte[] msg = (message + "\n").getBytes();
				try {
					clientSocket.getOutputStream().write(msg);
					Log.d(TAG, "Sent message: " + message);
					ePlayerName.setText("");
				} catch (IOException e) {
					Log.e(TAG, "IO error:" + e.getMessage());
					ePlayerName.setText("");
				}
			}
		}
	}

}
