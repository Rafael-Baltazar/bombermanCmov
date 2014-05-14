package com.example.bombermancmov.model.component;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import com.example.bombermancmov.wifi.CommandRequest;

public class NullPeerNetworkComponent extends PeerNetworkComponent {

	@Override
	public void createClientSocket(String hostname, int port) throws IOException {
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public List<CommandRequest> receiveCommandRequests()
			throws OptionalDataException, ClassNotFoundException, IOException {
				return new ArrayList<CommandRequest>();
	}

	@Override
	public void sendCommandRequests(List<CommandRequest> commandRequests)
			throws IOException {
	}

}
