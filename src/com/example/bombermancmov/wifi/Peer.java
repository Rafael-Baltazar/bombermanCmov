package com.example.bombermancmov.wifi;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;

/**
 * Contains the socket and the output and the input stream of a given peer.
 */
public class Peer {
	private SimWifiP2pSocket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	/**
	 * @param socket
	 * @param outputStream
	 * @param inputStream
	 */
	public Peer(SimWifiP2pSocket socket, ObjectOutputStream outputStream,
			ObjectInputStream inputStream) {
		super();
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	public SimWifiP2pSocket getSocket() {
		return socket;
	}

	public void setSocket(SimWifiP2pSocket socket) {
		this.socket = socket;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}
}
