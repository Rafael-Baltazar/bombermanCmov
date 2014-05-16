package com.example.bombermancmov.wifi;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pDevice;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.Channel;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.GroupInfoListener;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.utl.ist.cmov.wifidirect.service.SimWifiP2pService;
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
	private PeerListListener mPeerListener;
	private GroupInfoListener mGroupListener;
	/** Connection to bind and unbind wifiP2p service. */
	private ServiceConnection mConnection;
	private SimWifiP2pDevice mDevice;

	public WifiService(final Activity act) {
		mCallerAct = act;
		setDefaultPeerListener();
		setDefaultGroupListener();
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName className,
					IBinder service) {
				mMessenger = new Messenger(service);
				mManager = new SimWifiP2pManager(mMessenger);
				mChannel = mManager.initialize(act.getApplication(),
						act.getMainLooper(), null);
				mBound = true;
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

	public void setPeerListener(PeerListListener peerListener) {
		this.mPeerListener = peerListener;
	}

	public void setGroupListener(GroupInfoListener groupListener) {
		this.mGroupListener = groupListener;
	}

	public void setDefaultPeerListener() {
		this.mPeerListener = new PeerListListener() {
			@Override
			public void onPeersAvailable(SimWifiP2pDeviceList peers) {
				int size = peers.getDeviceList().size();
				Log.d(TAG + ":" + mCallerAct.getLocalClassName(),
						"Peer list size: " + size);
			}
		};
	}

	public void setDefaultGroupListener() {
		this.mGroupListener = new GroupInfoListener() {

			@Override
			public void onGroupInfoAvailable(SimWifiP2pDeviceList devices,
					SimWifiP2pInfo groupInfo) {
				String deviceName = groupInfo.getDeviceName();
				if (deviceName == null) {
					mDevice = null;
				} else {
					Log.d(TAG, "Device name: " + deviceName);
					mDevice = devices.getByName(deviceName);
					String virtualIp = mDevice.getVirtIp();
					Log.d(TAG, "Device ip: " + virtualIp);
					int virtualPort = mDevice.getVirtPort();
					Log.d(TAG, "Device port: " + virtualPort);
				}
			}
		};
	}

	public void requestPeers() {
		mManager.requestPeers(mChannel, mPeerListener);
	}

	public void requestGroupInfo() {
		mManager.requestGroupInfo(mChannel, mGroupListener);
	}

}
