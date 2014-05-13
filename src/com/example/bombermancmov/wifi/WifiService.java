package com.example.bombermancmov.wifi;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.Channel;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.utl.ist.cmov.wifidirect.service.SimWifiP2pService;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

public class WifiService {
	private static final String TAG = WifiService.class.getSimpleName();
	private Activity mCallerAct;
	private Channel mChannel = null;
	private Messenger mMessenger = null;
	private SimWifiP2pManager mManager = null;
	private boolean mBound = false;
	private PeerListListener peerRequester;
	/** Connection to bind and unbind wifiP2p service. */
	private ServiceConnection mConnection;

	public WifiService(final Activity act) {
		mCallerAct = act;
		peerRequester = new PeerListListener() {
			@Override
			public void onPeersAvailable(SimWifiP2pDeviceList peers) {
				int size = peers.getDeviceList().size();
				Log.d(TAG + ":" + mCallerAct.getLocalClassName(),
						"Peer list size: " + size);
			}
		};
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName className,
					IBinder service) {
				mMessenger = new Messenger(service);
				mManager = new SimWifiP2pManager(mMessenger);
				mChannel = mManager.initialize(act.getApplication(),
						act.getMainLooper(), null);
				mBound = true;
				mManager.requestPeers(mChannel, peerRequester);
				Log.d(TAG, "Service connected.");
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				mMessenger = null;
				mManager = null;
				mChannel = null;
				mBound = false;
				Log.d(TAG, "Service disconnected.");
			}
		};
	}

	public void bindService() {
		Intent intent = new Intent(mCallerAct, SimWifiP2pService.class);
		mCallerAct.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;
	}

	public void stopService() {
		if (mBound) {
			mCallerAct.unbindService(mConnection);
			Intent intent = new Intent(mCallerAct, SimWifiP2pService.class);
			mCallerAct.stopService(intent);
			mBound = false;
		}
	}

	public void requestPeers() {
		mManager.requestPeers(mChannel, peerRequester);
	}

}
