package com.example.bombermancmov;

import com.example.bombermancmov.wifi.WifiService;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainMenuActivity extends Activity {
	private static final String TAG = MainMenuActivity.class.getSimpleName();

	// WifiP2p
	private static final String REMOTE_IP = "192.168.0.2";
	private static final int REMOTE_PORT = 10001;
	private WifiService mWifi = new WifiService(this);

	private EditText ePlayerName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		SimWifiP2pSocketManager.Init(getApplicationContext());
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
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", name);
		startActivity(intent);
	}
	
	public void newMultiPlayerGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", name);
		intent.putExtra("isLocal", false);
		startActivity(intent);
	}

	public void newServer(View v) {
		Intent intent = new Intent(this, ServerActivity.class);
		startActivity(intent);
	}

	public void exit(View v) {
		finish();
	}

}
