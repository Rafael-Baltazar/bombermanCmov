package com.example.bombermancmov;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.example.bombermancmov.wifi.WifiService;

public class LobbyActivity extends ActionBarActivity {

	private WifiService mWifi = new WifiService(this);
	private EditText eIp;
	private String playerName;
	private String levelName;
	private SimWifiP2pBroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lobby);
		playerName = getIntent().getStringExtra("playerName");
		levelName = getIntent().getStringExtra("levelName");
		eIp = (EditText) findViewById(R.id.edit_ip_connect);
		mWifi.bindService();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
		mReceiver = new SimWifiP2pBroadcastReceiver(
				mWifi);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		mWifi.stopService();
		unregisterReceiver(mReceiver);
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
	
	public void exit(View v) {
		finish();
	}

}
