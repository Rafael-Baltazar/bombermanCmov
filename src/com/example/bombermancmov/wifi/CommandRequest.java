package com.example.bombermancmov.wifi;

import java.util.List;

public class CommandRequest {
	
	private String command;
	private List<String> args;
	
	/**
	 * @param command
	 * @param args
	 */
	public CommandRequest(String command, List<String> args) {
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

	public void setArgs(List<String> args) {
		this.args = args;
	}
}
