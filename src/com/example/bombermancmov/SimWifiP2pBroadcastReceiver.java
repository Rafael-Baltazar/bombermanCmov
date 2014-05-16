package com.example.bombermancmov;

import com.example.bombermancmov.wifi.WifiService;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

	private WifiService mWifi;

	/**
	 * @param mWifi
	 */
	public SimWifiP2pBroadcastReceiver(WifiService mWifi) {
		super();
		this.mWifi = mWifi;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			mWifi.requestGroupInfo();
			mWifi.requestPeers();
		} else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION
				.equals(action)) {
			SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent
					.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
		} else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION
				.equals(action)) {
			SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent
					.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
		}
	}
}
