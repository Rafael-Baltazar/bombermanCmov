package com.example.bombermancmov;

import com.example.bombermancmov.wifi.WifiService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class LobbyActivity extends ActionBarActivity {
	private static final String TAG = LobbyActivity.class.getSimpleName();
	
	private WifiService mWifi = new WifiService(this);
	private EditText eIp;
	private String playerName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lobby);
		playerName = getIntent().getStringExtra("playerName");
		eIp= (EditText) findViewById(R.id.edit_ip_connect);
		mWifi.bindService();
	}
	
	@Override
	protected void onDestroy() {
		mWifi.stopService();
		super.onDestroy();
	}

	public void newMultiPlayerGame(View v) {
		joinGame(true);
	}
	
	public void joinGame(boolean isMaster) {
		String masterIp = eIp.getText().toString();
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", playerName);
		intent.putExtra("isLocal", false);
		intent.putExtra("isMaster", isMaster);
		intent.putExtra("masterIp", masterIp);
		startActivity(intent);
	}
	
	public void connect(View v) {
		joinGame(false);
	}
	
}
