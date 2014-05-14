package com.example.bombermancmov.model.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import com.example.bombermancmov.wifi.CommandRequest;
import com.example.bombermancmov.wifi.CommandRequestUtil;
import com.example.bombermancmov.wifi.Peer;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;

/**
 * Component to create server, accept peers, and send or receive command
 * requests.
 */
public class MasterNetworkComponent extends NetworkComponent {

	private static int MAX_NUM_PEERS = 3;
	private List<Peer> mPeers = new ArrayList<Peer>();
	private SimWifiP2pSocketServer mSrvSocket = null;
	private int curPeerId = 0;

	/**
	 * Creates a new server socket in the component, if no other exists.
	 * 
	 * @param port
	 * @throws IOException
	 * @see #close()
	 */
	public void createServerSocket(int port) throws IOException {
		if (mSrvSocket == null) {
			mSrvSocket = new SimWifiP2pSocketServer(port);
		}
	}

	/**
	 * Close all peer and server sockets.
	 * 
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		for (Peer s : mPeers) {
			s.getOutputStream().close();
			s.getInputStream().close();
			s.getSocket().close();
		}
		mPeers.clear();
		closeServerSocket();
		curPeerId = 0;
	}

	public void closeServerSocket() throws IOException {
		if (mSrvSocket != null) {
			mSrvSocket.close();
			mSrvSocket = null;
		}
	}

	/**
	 * Blocks until a new peer joins. Then, initiates a protocol to give it an
	 * id and stores its socket in the component.
	 * 
	 * @return the new peer socket
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void acceptNewPeer() throws IOException, ClassNotFoundException {
		SimWifiP2pSocket newPeerSocket = mSrvSocket.accept();
		ObjectOutputStream os = new ObjectOutputStream(
				newPeerSocket.getOutputStream());
		os.flush();
		ObjectInputStream is = new ObjectInputStream(
				newPeerSocket.getInputStream());
		Peer newPeer = new Peer(newPeerSocket, os, is);
		
		Integer request = (Integer) is.readObject();
		if (request == PeerNetworkComponent.UNINITIALIZED) {
			if (mPeers.size() < MAX_NUM_PEERS) {
				curPeerId++;
				os.writeObject((Integer) curPeerId);
				mPeers.add(newPeer);
			} else {
				// Can't add peer.
				os.writeObject(request);
			}
		} else {
			os.writeObject(request);
			mPeers.add(newPeer);
		}
	}

	@Override
	public List<CommandRequest> receiveCommandRequests()
			throws OptionalDataException, ClassNotFoundException, IOException {
		List<CommandRequest> commandRequests = new ArrayList<CommandRequest>();
		for (Peer p : mPeers) {
			String commands = (String) p.getInputStream().readObject();
			commandRequests = CommandRequestUtil
					.translateCommandRequestString(commands);
		}
		return commandRequests;
	}

	@Override
	public void sendCommandRequests(List<CommandRequest> commandRequests)
			throws IOException {
		for (Peer p : mPeers) {
			String msg = CommandRequestUtil
					.codeCommandRequests(commandRequests);
			p.getOutputStream().writeObject(msg);
		}
	}

}
