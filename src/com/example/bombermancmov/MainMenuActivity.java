package com.example.bombermancmov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MainMenuActivity extends Activity {
	private static final String TAG = MainMenuActivity.class.getSimpleName();

	private EditText ePlayerName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		ePlayerName = (EditText) findViewById(R.id.ePlayerName);
		ePlayerName.setOnKeyListener(sendPlayerName);
	}

	private OnKeyListener sendPlayerName = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& keyCode == KeyEvent.KEYCODE_ENTER) {
				EditText ev = (EditText) v;
				String name = ev.getText().toString();
				ev.setText("");

				Log.d(TAG, "player name: " + name);

				return true;
			}
			return false;
		}
	};

	public void newSingleGame(View v) {
		String name = ePlayerName.getText().toString();
		Intent intent = new Intent(this, SingleGameActivity.class);
		intent.putExtra("PlayerName", name);
		this.startActivity(intent);
	}

	public void exit(View v) {
		finish();
	}

}
