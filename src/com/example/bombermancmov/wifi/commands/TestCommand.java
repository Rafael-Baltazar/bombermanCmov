package com.example.bombermancmov.wifi.commands;

import java.util.List;

import android.app.Activity;
import android.widget.EditText;

public class TestCommand extends Command {

	public static final String CODE = "test";
	private Activity mActivity;
	private EditText eRcvText;

	/**
	 * @param mActivity
	 * @param eRcvText
	 */
	public TestCommand(Activity mActivity, EditText eRcvText) {
		super();
		this.mActivity = mActivity;
		this.eRcvText = eRcvText;
	}

	@Override
	public void execute(List<String> args) {
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				eRcvText.setText("Received");
			}
		});
	}

}
