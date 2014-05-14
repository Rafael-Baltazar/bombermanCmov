package com.example.bombermancmov.model.component;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.List;

import com.example.bombermancmov.wifi.CommandRequest;

public abstract class NetworkComponent {
	/**
	 * Closes all sockets.
	 * 
	 * @throws IOException
	 */
	public abstract void close() throws IOException;

	/**
	 * Reads command requests from all open sockets.
	 * 
	 * @return
	 * @throws OptionalDataException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public abstract List<CommandRequest> receiveCommandRequests()
			throws OptionalDataException, ClassNotFoundException, IOException;

	/**
	 * Sends the command requests to all open sockets.
	 * 
	 * @param commandRequests
	 * @throws IOException
	 */
	public abstract void sendCommandRequests(
			List<CommandRequest> commandRequests) throws IOException;
}
