package com.example.bombermancmov.wifi;

import java.util.List;

public class WifiMessage {
	
	private String command;
	private List<String> args;
	
	/**
	 * @param command
	 * @param args
	 */
	public WifiMessage(String command, List<String> args) {
		super();
		this.command = command;
		this.args = args;
	}

	public String getCommand() {
		return command; 
	}
	
	public List<String> getArgs() {
		return args;
	}
}
