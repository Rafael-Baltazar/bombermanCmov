package com.example.bombermancmov;

import java.util.Collection;

import com.example.bombermancmov.wifi.WifiService;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pDevice;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LobbyActivity extends ActionBarActivity {
	private static final String TAG = LobbyActivity.class.getSimpleName();
	
	private ArrayAdapter<String> adapter;
	private WifiService mWifi = new WifiService(this);
	private PeerListListener peerListener = new PeerListListener() {
		
		@Override
		public void onPeersAvailable(SimWifiP2pDeviceList peers) {
			refreshDevices(peers.getDeviceList());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lobby);
		adapter = new ArrayAdapter<String>(this, R.layout.available_game);
		ListView l = (ListView) findViewById(R.id.availableGames);
		l.setAdapter(adapter);
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

	public void refreshDevices(Collection<SimWifiP2pDevice> devices) {
		Log.d(TAG, "Peer list size: " + devices.size());
		adapter.clear();
		for(SimWifiP2pDevice d : devices) {
			adapter.add(d.deviceName);
		}
		adapter.notifyDataSetChanged();
	}
	
	public void refreshAvailableGames(View v) {
		mWifi.requestPeers();
	}
}
