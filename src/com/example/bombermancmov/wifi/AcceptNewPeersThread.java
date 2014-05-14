package com.example.bombermancmov.wifi;

import java.io.IOException;

import android.util.Log;

import com.example.bombermancmov.model.component.MasterNetworkComponent;

public class AcceptNewPeersThread extends Thread {
	private static final String TAG = AcceptNewPeersThread.class
			.getSimpleName();

	private MasterNetworkComponent mNetComponent;

	/**
	 * @param netComponent
	 */
	public AcceptNewPeersThread(MasterNetworkComponent netComponent) {
		super();
		mNetComponent = netComponent;
	}

	public void close() {
		try {
			mNetComponent.close();
		} catch (IOException e) {
			Log.e(TAG, "Close sockets: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				mNetComponent.acceptNewPeer();
			} catch (IOException e) {
				Log.e(TAG, "IO Accepting new peer: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e(TAG,
						"ClassNotFound Accepting new peer: " + e.getMessage());
			}
		}
	}
}
