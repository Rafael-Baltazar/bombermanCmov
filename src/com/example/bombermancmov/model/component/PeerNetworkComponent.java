package com.example.bombermancmov.model.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;

import com.example.bombermancmov.wifi.CommandRequest;
import com.example.bombermancmov.wifi.CommandRequestUtil;
import com.example.bombermancmov.wifi.Peer;

public class PeerNetworkComponent extends NetworkComponent {

	public static final int UNINITIALIZED = -1;
	private Peer mMaster = null;
	private int mPlayerId = UNINITIALIZED;

	/**
	 * Connects to a server socket and keeps the socket, if no other exists.
	 * 
	 * @param hostname
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @see #close()
	 */
	public void createClientSocket(String hostname, int port)
			throws IOException, ClassNotFoundException {
		if (mMaster != null) {
			return;
		}
		SimWifiP2pSocket client = new SimWifiP2pSocket(hostname, port);
		ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
		os.flush();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());
		Peer peer = new Peer(client, os, is);

		mMaster = peer;
		Integer request = mPlayerId;
		os.writeObject(request);
		mPlayerId = (Integer) is.readObject();
	}

	@Override
	public void close() throws IOException {
		if (mMaster != null) {
			mMaster.getInputStream().close();
			mMaster.getOutputStream().close();
			mMaster.getSocket().close();
		}
	}

	@Override
	public List<CommandRequest> receiveCommandRequests()
			throws OptionalDataException, ClassNotFoundException, IOException {
		List<CommandRequest> commandRequests = new ArrayList<CommandRequest>();
		String commands = (String) mMaster.getInputStream().readObject();
		commandRequests = CommandRequestUtil
				.translateCommandRequestString(commands);
		return commandRequests;
	}

	@Override
	public void sendCommandRequests(List<CommandRequest> commandRequests)
			throws IOException {
		String msg = CommandRequestUtil.codeCommandRequests(commandRequests);
		mMaster.getOutputStream().writeObject(msg);
	}

	public int getPlayerId() {
		return mPlayerId;
	}

}
