package com.example.bombermancmov;

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

	public void newSingleGame(View v) {
		String name = ePlayerName.getText().toString();		
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("playerName", name);
		intent.putExtra("levelName", this.spinnerValue);
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
