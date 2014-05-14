package com.example.bombermancmov;

import com.example.bombermancmov.wifi.WifiService;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainMenuActivity extends Activity {
	private static final String TAG = MainMenuActivity.class.getSimpleName();

	// WifiP2p
	private static final String REMOTE_IP = "192.168.0.2";
	private static final int REMOTE_PORT = 10001;
	private WifiService mWifi = new WifiService(this);

	private EditText ePlayerName;
	private String spinnerValue;
	private Spinner spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		SimWifiP2pSocketManager.Init(getApplicationContext());
		ePlayerName = (EditText) findViewById(R.id.ePlayerName);
		ePlayerName.setText("Dave Default");
		
		//Level spinner
		this.spinner = (Spinner) findViewById(R.id.level_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.level_strings, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		//Set listener to get current element
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        spinnerValue = (String) parent.getItemAtPosition(pos);
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    	spinnerValue = "Level 1"; //Default
		    }
		});
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

	public void newSingleGame(View v) {
		String name = ePlayerName.getText().toString();		
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", name);
		intent.putExtra("levelName", this.spinnerValue);
		startActivity(intent);
	}
	
	public void newMultiPlayerGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", name);
		intent.putExtra("isLocal", false);
		startActivity(intent);
	}

	public void newServer(View v) {
		Intent intent = new Intent(this, ServerActivity.class);
		startActivity(intent);
	}

	public void exit(View v) {
		finish();
	}

}
