package com.example.bombermancmov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.example.bombermancmov.wifi.WifiService;

public class LobbyActivity extends ActionBarActivity {
	private static final String TAG = LobbyActivity.class.getSimpleName();
	
	private WifiService mWifi = new WifiService(this);
	private EditText eIp;
	private String playerName;
	private String levelName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lobby);
		playerName = getIntent().getStringExtra("playerName");
		levelName = getIntent().getStringExtra("levelName");
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
		intent.putExtra("levelName", levelName);
		startActivity(intent);
	}
	
	public void connect(View v) {
		joinGame(false);
	}
	
}
