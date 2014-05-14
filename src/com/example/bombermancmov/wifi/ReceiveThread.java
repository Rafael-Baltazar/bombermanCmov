package com.example.bombermancmov.wifi;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.example.bombermancmov.model.component.NetworkComponent;
import com.example.bombermancmov.wifi.commands.Command;

public class ReceiveThread extends Thread {
	private static final String TAG = ReceiveThread.class.getSimpleName();
	private NetworkComponent mNetComponent;
	private Map<String, Command> mCommands;

	/**
	 * @param mNetComponent
	 * @param commands
	 */
	public ReceiveThread(NetworkComponent mNetComponent,
			Map<String, Command> commands) {
		super();
		this.mNetComponent = mNetComponent;
		mCommands = commands;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				List<CommandRequest> commandRequests = mNetComponent
						.receiveCommandRequests();
				for(CommandRequest req : commandRequests) {
					String cmd = req.getCommand();
					List<String> args = req.getArgs();
					mCommands.get(cmd).execute(args);
				}
			} catch (OptionalDataException e) {
				Log.e(TAG, "Received primitive: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "ClassNotFound: " + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IO: " + e.getMessage());
			}
		}
	}

}
