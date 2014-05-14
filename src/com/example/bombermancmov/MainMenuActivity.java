package com.example.bombermancmov;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainMenuActivity extends Activity {

	private EditText ePlayerName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		SimWifiP2pSocketManager.Init(getApplicationContext());
		ePlayerName = (EditText) findViewById(R.id.ePlayerName);
	}

	public void newSingleGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", name);
		startActivity(intent);
	}

	public void newMultiPlayerGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, LobbyActivity.class);
		intent.putExtra("playerName", name);
		startActivity(intent);
	}

	public void exit(View v) {
		finish();
	}

}
