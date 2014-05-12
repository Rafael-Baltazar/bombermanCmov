package com.example.bombermancmov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bombermancmov.wifi.SocketUtil;
import com.example.bombermancmov.wifi.WifiService;
import com.example.bombermancmov.wifi.commands.Command;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ServerActivity extends ActionBarActivity {
	private static final String TAG = ServerActivity.class.getSimpleName();

	// WifiP2p
	private static final int SERVER_PORT = 10001;
	private boolean mSrvRegistered = false;
	private WifiService mWifi = new WifiService(this);
	private Map<String, Command> mCommands = new HashMap<String, Command>();

	// Layout
	private EditText eRcvText;
	private Button acceptButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		// Layout
		eRcvText = (EditText) findViewById(R.id.rcvText);
		acceptButton = (Button) findViewById(R.id.btnAcceptClient);
		initSrvCommands();
	}

	@Override
	protected void onResume() {
		mWifi.bindService();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mWifi.stopService();
		super.onPause();
	}

	public void acceptClient(View v) {
		if (!mSrvRegistered) {
			SocketUtil.registerServer(mCommands);
			mSrvRegistered = true;
		}
		acceptButton.setEnabled(false);
	}

	public void requestPeers(View v) {
		mWifi.requestPeers();
	}

	private void initSrvCommands() {
		Command cmd = new Command() {

			@Override
			public void execute(List<String> args) {
				String text = args.get(0);
				eRcvText.setText(text);
			}
		};
		mCommands.put("rcv", cmd);
	}

}
