package com.example.bombermancmov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;

import com.example.bombermancmov.wifi.WifiConnection;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ServerActivity extends ActionBarActivity {
	private static final String TAG = ServerActivity.class.getSimpleName();

	// WifiP2p
	private static final int SERVER_PORT = 10001;
	private SimWifiP2pSocketServer mSrvSocket;
	private WifiConnection mWifi = new WifiConnection(this);

	// Layout
	private EditText eRcvText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		// WifiP2p
		SimWifiP2pSocketManager.Init(getApplicationContext());

		// Layout
		eRcvText = (EditText) findViewById(R.id.rcvText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		try {
			mSrvSocket = new SimWifiP2pSocketServer(SERVER_PORT);
		} catch (IOException e) {
			Log.e(TAG, "IO new SocketServer: " + e.getMessage());
		}
		mWifi.bindWifiService();
		super.onResume();
	}

	@Override
	protected void onPause() {
		try {
			mSrvSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "IO close SocketServer: " + e.getMessage());
		}
		mWifi.unbindService();
		super.onPause();
	}

	public void acceptClient(View v) {
		new ReadFromTask().execute(mSrvSocket);
	}

	public void requestPeers(View v) {
		mWifi.getManager().requestPeers(mWifi.getChannel(),
				mWifi.getPeerRequester());
	}

	// WifiP2p
	public void processWifiMessage(String message) {
		String prevTxt = eRcvText.getText().toString();
		eRcvText.setText(message + prevTxt);
	}
	/**
	 * Receive a message and store it in eRcvText.
	 */
	private class ReadFromTask extends
			AsyncTask<SimWifiP2pSocketServer, Void, String> {

		@Override
		protected String doInBackground(SimWifiP2pSocketServer... params) {
			SimWifiP2pSocketServer srvSocket = params[0];
			SimWifiP2pSocket client = null;
			BufferedReader br;
			try {
				client = srvSocket.accept();
			} catch (IOException e) {
				Log.e(TAG, "IO: " + e.getMessage());
				return null;
			}
			try {
				br = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				String message = br.readLine();
				client.close();
				return message;
			} catch (IOException e) {
				Log.e(TAG, "IO: " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Log.d(TAG, result);
				processWifiMessage(result);
			}
			super.onPostExecute(result);
		}
	}

}
